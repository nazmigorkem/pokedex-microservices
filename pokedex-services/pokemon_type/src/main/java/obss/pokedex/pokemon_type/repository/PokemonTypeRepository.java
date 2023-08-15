package obss.pokedex.pokemon_type.repository;

import obss.pokedex.pokemon_type.entity.PokemonType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {
}
