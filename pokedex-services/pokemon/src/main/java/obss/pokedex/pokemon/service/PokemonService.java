package obss.pokedex.pokemon.service;

import jakarta.transaction.Transactional;
import obss.pokedex.pokemon.client.UserServiceClient;
import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.model.PokemonUpdateRequest;
import obss.pokedex.pokemon.model.kafka.PokemonDeletion;
import obss.pokedex.pokemon.model.kafka.UserListUpdate;
import obss.pokedex.pokemon.repository.PokemonRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokemonTypeService pokemonTypeService;
    private final UserServiceClient userServiceClient;
    private final KafkaTemplate<String, PokemonDeletion> kafkaTemplate;

    public PokemonService(PokemonRepository pokemonRepository, PokemonTypeService pokemonTypeService, UserServiceClient userServiceClient, KafkaTemplate<String, PokemonDeletion> kafkaTemplate) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonTypeService = pokemonTypeService;
        this.userServiceClient = userServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public PokemonResponse addPokemon(PokemonAddRequest pokemonAddRequest) {
        return pokemonRepository.save(pokemonAddRequest.toPokemon(pokemonTypeService)).toPokemonResponse();
    }

    public PokemonResponse updatePokemon(PokemonUpdateRequest pokemonUpdateRequest) {
        Pokemon pokemon = pokemonRepository.findByNameIgnoreCase(pokemonUpdateRequest.getSearchName()).orElseThrow();
        pokemonUpdateRequest.updatePokemon(pokemon, pokemonTypeService);
        return pokemonRepository.save(pokemon).toPokemonResponse();
    }

    public PokemonResponse getPokemonByName(String name) {
        throwServiceExceptionIfPokemonDoesNotExistWithName(name);
        return pokemonRepository.findByNameIgnoreCase(name).orElseThrow().toPokemonResponse();
    }

    public Page<PokemonResponse> getPokemonPageByStartsWithName(String name, int page, int size) {
        return pokemonRepository.findAllByNameStartsWithIgnoreCase(name, PageRequest.of(page, size)).map(Pokemon::toPokemonResponse);
    }

    public void deletePokemonByName(String name) {
        throwServiceExceptionIfPokemonDoesNotExistWithName(name);
        pokemonRepository.findByNameIgnoreCase(name).ifPresent(x -> {
            var pokemonDeletionRequest = new PokemonDeletion();
            pokemonDeletionRequest.setCatchListedUsers(x.getCatchListedUsers().stream().toList());
            pokemonDeletionRequest.setWishListedUsers(x.getWishListedUsers().stream().toList());
            pokemonDeletionRequest.setPokemonId(x.getId());
            pokemonRepository.delete(x);
            kafkaTemplate.send("pokemon-deletion", new PokemonDeletion(x.getId(), x.getCatchListedUsers().stream().toList(), x.getWishListedUsers().stream().toList()));
        });
    }

    @KafkaListener(topics = "user-wish-list-addition", groupId = "pokemon-service")
    @Transactional
    public void addUserToWishListed(UserListUpdate userListUpdate) {
        pokemonRepository.findById(userListUpdate.getPokemonId()).ifPresent(pokemon -> {
            Hibernate.initialize(pokemon.getWishListedUsers());
            if (pokemon.getWishListedUsers() == null) {
                pokemon.setWishListedUsers(new HashSet<>());
            }
            pokemon.getWishListedUsers().add(userListUpdate.getUserId());
            pokemonRepository.save(pokemon);
        });
    }

    @KafkaListener(topics = "user-wish-list-deletion", groupId = "pokemon-service")
    @Transactional
    public void deleteUserFromWishListed(UserListUpdate userListUpdate) {
        pokemonRepository.findById(userListUpdate.getPokemonId()).ifPresent(pokemon -> {
            Hibernate.initialize(pokemon.getWishListedUsers());
            if (pokemon.getWishListedUsers() == null) {
                return;
            }
            pokemon.getWishListedUsers().remove(userListUpdate.getUserId());
            pokemonRepository.save(pokemon);
        });
    }

    @KafkaListener(topics = "user-catch-list-addition", groupId = "pokemon-service")
    @Transactional
    public void addUserToCatchListed(UserListUpdate userListUpdate) {
        pokemonRepository.findById(userListUpdate.getPokemonId()).ifPresent(pokemon -> {
            Hibernate.initialize(pokemon.getCatchListedUsers());
            if (pokemon.getCatchListedUsers() == null) {
                pokemon.setCatchListedUsers(new HashSet<>());
            }
            pokemon.getCatchListedUsers().add(userListUpdate.getUserId());
            pokemonRepository.save(pokemon);
        });
    }

    @KafkaListener(topics = "user-catch-list-deletion", groupId = "pokemon-service")
    @Transactional
    public void deleteUserFromCatchListed(UserListUpdate userListUpdate) {
        pokemonRepository.findById(userListUpdate.getPokemonId()).ifPresent(pokemon -> {
            Hibernate.initialize(pokemon.getCatchListedUsers());
            if (pokemon.getCatchListedUsers() == null) {
                return;
            }
            pokemon.getCatchListedUsers().remove(userListUpdate.getUserId());
            pokemonRepository.save(pokemon);
        });
    }
    /*
        GUARD CLAUSES
     */

    private void throwServiceExceptionIfPokemonDoesNotExistWithName(String name) {
        if (!pokemonRepository.existsByNameIgnoreCase(name))
            throw ServiceException.PokemonWithNameNotFound(name);
    }

    public List<PokemonResponse> getAllPokemonsByListQuery(List<UUID> uuids) {
        return pokemonRepository.findAllByIdIn(uuids).stream().map(Pokemon::toPokemonResponse).toList();
    }

}
