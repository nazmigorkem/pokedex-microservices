package obss.pokedex.pokemon.controller;

import jakarta.validation.Valid;
import obss.pokedex.pokemon.model.UserAddPokemonRequest;
import obss.pokedex.pokemon.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list/wish")
public class WishListController {
    private final PokemonService pokemonService;

    public WishListController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addUserToWishListed(@Valid @RequestBody UserAddPokemonRequest userAddPokemonRequest) {
        pokemonService.addUserToWishListed(userAddPokemonRequest);
        return ResponseEntity.ok().build();
    }
}
