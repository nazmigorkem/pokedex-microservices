package obss.pokedex.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "pokemon-service", path = "/api/pokemon")
public interface PokemonServiceClient {


}
