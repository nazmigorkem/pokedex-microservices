package obss.pokedex.pokemon.repository;

import obss.pokedex.pokemon.entity.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PokemonRepository extends JpaRepository<Pokemon, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Pokemon> findByNameIgnoreCase(String name);

    Page<Pokemon> findAllByNameStartsWithIgnoreCase(String name, Pageable pageable);
}
