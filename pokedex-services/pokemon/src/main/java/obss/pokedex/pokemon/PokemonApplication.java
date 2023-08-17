package obss.pokedex.pokemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PokemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokemonApplication.class, args);
    }

}
