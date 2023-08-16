package obss.pokedex.pokemon.controller;

import jakarta.validation.Valid;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping("/add")
    public ResponseEntity<PokemonResponse> addPokemon(@Valid @RequestBody PokemonAddRequest pokemonAddRequest) {
        return ResponseEntity.ok(pokemonService.addPokemon(pokemonAddRequest));
    }
}
