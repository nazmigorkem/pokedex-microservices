package obss.pokedex.pokemon.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPokemonRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String pokemonName;
}
