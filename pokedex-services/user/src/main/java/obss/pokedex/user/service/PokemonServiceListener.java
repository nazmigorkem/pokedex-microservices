package obss.pokedex.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import obss.pokedex.user.model.kafka.PokemonDeletion;
import obss.pokedex.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PokemonServiceListener {
    private final UserRepository userRepository;

    public PokemonServiceListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "pokemon-deletion", groupId = "user-service")
    @Transactional
    public void listenPokemonDeletion(PokemonDeletion pokemonDeletion) {
        try {
            pokemonDeletion.getCatchListedUsers().forEach(uuid -> {
                var user = userRepository.findById(uuid).orElseThrow();
                if (user.getCatchList() != null) {
                    Hibernate.initialize(user.getCatchList());
                    user.getCatchList().remove(pokemonDeletion.getPokemonId());
                    userRepository.save(user);
                }
            });

            pokemonDeletion.getWishListedUsers().forEach(uuid -> {
                var user = userRepository.findById(uuid).orElseThrow();
                if (user.getWishList() != null) {
                    Hibernate.initialize(user.getWishList());
                    user.getWishList().remove(pokemonDeletion.getPokemonId());
                    userRepository.save(user);
                }
            });
        } catch (Exception e) {
            log.error("Error occurred while deleting pokemon from users' lists", e);
        }
    }
}
