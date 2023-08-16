package obss.pokedex.pokemon.controller;

import jakarta.validation.Valid;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.model.PokemonUpdateRequest;
import obss.pokedex.pokemon.service.PokemonService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePokemon(@RequestParam(defaultValue = "") String name) {
        pokemonService.deletePokemonByName(name);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<PokemonResponse> updatePokemon(@Valid @RequestBody PokemonUpdateRequest pokemonUpdateRequest) {
        return ResponseEntity.ok(pokemonService.updatePokemon(pokemonUpdateRequest));
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<PokemonResponse> getPokemonByName(@PathVariable String name) {
        return ResponseEntity.ok(pokemonService.getPokemonByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PokemonResponse>> getPokemonPageByStartsWithName(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pokemonService.getPokemonPageByStartsWithName(name, page, size));
    }
}
