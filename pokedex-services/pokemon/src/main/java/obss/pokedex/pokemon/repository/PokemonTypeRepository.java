package obss.pokedex.pokemon.repository;

import obss.pokedex.pokemon.entity.PokemonType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {
}
