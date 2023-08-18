package obss.pokedex.pokemon.client;

import obss.pokedex.pokemon.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/user-service/user")
public interface UserServiceClient {
    @GetMapping("/search/{username}")
    ResponseEntity<UserResponse> getUserByName(@PathVariable String username);
}
