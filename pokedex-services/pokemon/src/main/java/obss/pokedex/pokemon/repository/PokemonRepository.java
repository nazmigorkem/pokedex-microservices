package obss.pokedex.pokemon.repository;

import obss.pokedex.pokemon.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    boolean existsByNameIgnoreCase(String name);
}
