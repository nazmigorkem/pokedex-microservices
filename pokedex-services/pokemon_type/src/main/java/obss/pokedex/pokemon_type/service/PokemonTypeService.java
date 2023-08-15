package obss.pokedex.pokemon_type.service;

import obss.pokedex.pokemon_type.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon_type.model.PokemonTypeResponse;
import obss.pokedex.pokemon_type.repository.PokemonTypeRepository;
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
}
