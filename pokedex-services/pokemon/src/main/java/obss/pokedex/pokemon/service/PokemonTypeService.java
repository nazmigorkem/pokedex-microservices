package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PokemonTypeService {
    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    public PokemonTypeResponse addPokemonType(PokemonTypeAddRequest pokemonTypeAddRequest) {
        throwServiceExceptionIfPokemonTypeExistsWithName(pokemonTypeAddRequest.getName());
        pokemonTypeRepository.save(pokemonTypeAddRequest.toPokemonType());
        return PokemonTypeResponse.builder().name(pokemonTypeAddRequest.getName()).color(pokemonTypeAddRequest.getColor()).build();
    }

    /*****************
     * GUARD CLAUSES *
     *****************/

    public void throwServiceExceptionIfPokemonTypeExistsWithName(String name) {
        if (pokemonTypeRepository.existsByName(name)) {
            throw ServiceException.PokemonTypeWithNameAlreadyExists(name);
        }
    }
}
