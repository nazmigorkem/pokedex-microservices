package obss.pokedex.pokemon.repository;

import obss.pokedex.pokemon.entity.PokemonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Optional<PokemonType> findByNameIgnoreCase(String name);

    Page<PokemonType> findAllByNameStartsWithIgnoreCase(String name, Pageable pageable);
}
