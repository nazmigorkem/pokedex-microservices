package obss.pokedex.user.client;

import obss.pokedex.user.model.PokemonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "pokemon-service", path = "/api/pokemon-service/pokemon")
public interface PokemonServiceClient {
    @GetMapping("/search/{name}")
    ResponseEntity<PokemonResponse> getPokemonByName(@PathVariable String name);
}
