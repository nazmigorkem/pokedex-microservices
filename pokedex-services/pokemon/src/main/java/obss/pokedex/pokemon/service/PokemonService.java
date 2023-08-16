package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.model.PokemonUpdateRequest;
import obss.pokedex.pokemon.repository.PokemonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokemonTypeService pokemonTypeService;

    public PokemonService(PokemonRepository pokemonRepository, PokemonTypeService pokemonTypeService) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonTypeService = pokemonTypeService;
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
        pokemonRepository.findByNameIgnoreCase(name).ifPresent(pokemonRepository::delete);
    }

    /*
        GUARD CLAUSES
     */

    private void throwServiceExceptionIfPokemonDoesNotExistWithName(String name) {
        if (!pokemonRepository.existsByNameIgnoreCase(name))
            throw ServiceException.PokemonWithNameNotFound(name);
    }
}
