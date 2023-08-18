package obss.pokedex.user.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PokemonTypeResponse {
    private String name;
    private String color;
}
