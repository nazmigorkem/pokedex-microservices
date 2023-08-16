package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import obss.pokedex.pokemon.model.PokemonTypeUpdateRequest;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PokemonTypeService {
    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonTypeService(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    public PokemonTypeResponse addPokemonType(PokemonTypeAddRequest pokemonTypeAddRequest) {
        pokemonTypeRepository.save(pokemonTypeAddRequest.toPokemonType());
        return PokemonTypeResponse.builder().name(pokemonTypeAddRequest.getName()).color(pokemonTypeAddRequest.getColor()).build();
    }

    public void deletePokemonTypeName(String name) {
        pokemonTypeRepository.findByName(name).ifPresent(pokemonTypeRepository::delete);
    }

    public PokemonTypeResponse updatePokemonType(PokemonTypeUpdateRequest pokemonTypeUpdateRequest) {
        var pokemonType = pokemonTypeRepository.findByName(pokemonTypeUpdateRequest.getSearchName()).orElseThrow();
        pokemonTypeRepository.save(pokemonTypeUpdateRequest.mapToPokemonType(pokemonType));
        return PokemonTypeResponse.builder().name(pokemonType.getName()).color(pokemonType.getColor()).build();
    }
}
