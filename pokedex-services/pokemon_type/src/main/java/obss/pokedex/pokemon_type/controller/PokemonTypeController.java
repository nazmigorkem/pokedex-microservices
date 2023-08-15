package obss.pokedex.pokemon_type.controller;

import obss.pokedex.pokemon_type.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon_type.model.PokemonTypeResponse;
import obss.pokedex.pokemon_type.service.PokemonTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Validated
public class PokemonTypeController {

    private final PokemonTypeService pokemonTypeService;

    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    @PostMapping("/pokemon-type/add")
    public ResponseEntity<PokemonTypeResponse> getPokemonTypes(@RequestBody PokemonTypeAddRequest pokemonTypeAddRequest) {
        return ResponseEntity.ok(pokemonTypeService.addPokemonType(pokemonTypeAddRequest));
    }
}
