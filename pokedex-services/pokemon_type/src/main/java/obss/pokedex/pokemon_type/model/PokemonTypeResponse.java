package obss.pokedex.pokemon_type.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PokemonTypeResponse {
    private String name;
    private String color;
}
