package obss.pokedex.user.client;

import obss.pokedex.user.model.PokemonResponse;
import obss.pokedex.user.model.UserAddPokemonRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pokemon-service", path = "/api/pokemon-service")
public interface PokemonServiceClient {
    @GetMapping("/pokemon/search/{name}")
    ResponseEntity<PokemonResponse> getPokemonByName(@PathVariable String name);

    @PostMapping("/list/wish/add")
    ResponseEntity<Void> addUserToWishListed(@RequestBody UserAddPokemonRequest pokemonAddUserToWishListedRequest);
}
