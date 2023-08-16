package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.repository.PokemonRepository;
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
}
