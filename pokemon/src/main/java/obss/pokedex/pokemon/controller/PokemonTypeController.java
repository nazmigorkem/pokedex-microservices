package obss.pokedex.pokemon.controller;

import jakarta.validation.Valid;
import obss.pokedex.pokemon.model.PokemonTypeAddRequest;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import obss.pokedex.pokemon.model.PokemonTypeUpdateRequest;
import obss.pokedex.pokemon.service.PokemonTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pokemon-type")
public class PokemonTypeController {

    private final PokemonTypeService pokemonTypeService;

    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<PokemonTypeResponse> getPokemonTypes(@Valid @RequestBody PokemonTypeAddRequest pokemonTypeAddRequest) {
        return ResponseEntity.ok(pokemonTypeService.addPokemonType(pokemonTypeAddRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePokemonTypes(@RequestParam(defaultValue = "") String name) {
        pokemonTypeService.deletePokemonTypeName(name);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<PokemonTypeResponse> updatePokemonTypes(@Valid @RequestBody PokemonTypeUpdateRequest pokemonTypeUpdateRequest) {
        return ResponseEntity.ok(pokemonTypeService.updatePokemonType(pokemonTypeUpdateRequest));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<PokemonTypeResponse> getPokemonTypeByName(@PathVariable String name) {
        return ResponseEntity.ok(pokemonTypeService.getPokemonTypeByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PokemonTypeResponse>> getPokemonTypePageByStartsWithName(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pokemonTypeService.getPokemonTypePageByStartsWithName(name, page, size));
    }
}
