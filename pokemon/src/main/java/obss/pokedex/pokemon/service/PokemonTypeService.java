package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.entity.PokemonType;
import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import obss.pokedex.pokemon.model.PokemonTypeUpdateRequest;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PokemonTypeService {
    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    public PokemonTypeResponse getPokemonTypeByName(String name) {
        throwServiceExceptionIfPokemonTypeDoesNotExistWithName(name);
        var pokemonType = pokemonTypeRepository.findByNameIgnoreCase(name).orElseThrow();
        return pokemonType.toPokemonTypeResponse();
    }

    public Page<PokemonTypeResponse> getPokemonTypePageByStartsWithName(String name, int page, int size) {
        return pokemonTypeRepository.findAllByNameStartsWithIgnoreCase(name, PageRequest.of(page, size)).map(PokemonType::toPokemonTypeResponse);
    }

    public PokemonType getPokemonTypeEntityByName(String name) {
        throwServiceExceptionIfPokemonTypeDoesNotExistWithName(name);
        return pokemonTypeRepository.findByNameIgnoreCase(name).orElseThrow();
    }

    public PokemonTypeResponse addPokemonType(PokemonTypeAddRequest pokemonTypeAddRequest) {
        var pokemonType = pokemonTypeRepository.save(pokemonTypeAddRequest.toPokemonType());
        return pokemonType.toPokemonTypeResponse();
    }

    public void deletePokemonTypeName(String name) {
        throwServiceExceptionIfPokemonTypeDoesNotExistWithName(name);
        pokemonTypeRepository.findByNameIgnoreCase(name).ifPresent(pokemonTypeRepository::delete);
    }

    public PokemonTypeResponse updatePokemonType(PokemonTypeUpdateRequest pokemonTypeUpdateRequest) {
        var pokemonType = pokemonTypeRepository.findByNameIgnoreCase(pokemonTypeUpdateRequest.getSearchName()).orElseThrow();
        pokemonTypeUpdateRequest.updatePokemonType(pokemonType);
        return pokemonTypeRepository.save(pokemonType).toPokemonTypeResponse();
    }

    /*
        GUARD CLAUSES
     */

    private void throwServiceExceptionIfPokemonTypeDoesNotExistWithName(String name) {
        if (!pokemonTypeRepository.existsByNameIgnoreCase(name))
            throw ServiceException.PokemonTypeWithNameNotFound(name);
    }
}
