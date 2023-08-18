package obss.pokedex.user.client;

import obss.pokedex.user.model.PokemonResponse;
import obss.pokedex.user.model.UserPokemonRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "pokemon-service", path = "/api/pokemon-service")
public interface PokemonServiceClient {
    @GetMapping("/pokemon/search/{name}")
    ResponseEntity<PokemonResponse> getPokemonByName(@PathVariable String name);

    @GetMapping("/pokemon/getAll")
    ResponseEntity<List<PokemonResponse>> getAllPokemonsByListQuery(@RequestParam List<UUID> uuids);

    @PostMapping("/list/wish/add")
    ResponseEntity<Void> addUserToWishListed(@RequestBody UserPokemonRequest UserPokemonRequest);

    @PostMapping("/list/wish/delete")
    ResponseEntity<Void> deleteUserFromWishListed(@RequestBody UserPokemonRequest userPokemonRequest);


    @PostMapping("/list/catch/add")
    ResponseEntity<Void> addUserToCatchListed(@RequestBody UserPokemonRequest UserPokemonRequest);

    @PostMapping("/list/catch/delete")
    ResponseEntity<Void> deleteUserFromCatchListed(@RequestBody UserPokemonRequest userPokemonRequest);
}
