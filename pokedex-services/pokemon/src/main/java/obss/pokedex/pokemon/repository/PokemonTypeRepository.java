package obss.pokedex.pokemon.repository;

import obss.pokedex.pokemon.entity.PokemonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<PokemonType> findByNameIgnoreCase(String name);
}
