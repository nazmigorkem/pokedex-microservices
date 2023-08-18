package obss.pokedex.pokemon.controller;

import jakarta.validation.Valid;
import obss.pokedex.pokemon.model.UserPokemonRequest;
import obss.pokedex.pokemon.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list/catch")
public class CatchListController {

    private final PokemonService pokemonService;

    public CatchListController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addUserToWishListed(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        pokemonService.addUserToCatchListed(userPokemonRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUserToWishListed(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        pokemonService.deleteUserFromCatchListed(userPokemonRequest);
        return ResponseEntity.ok().build();
    }
}
