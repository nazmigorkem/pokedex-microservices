package obss.pokedex.pokemon.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import obss.pokedex.pokemon.entity.PokemonType;

@Data
public class PokemonTypeAddRequest {
    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotBlank(message = "Color cannot be blank.")
    @Size(min = 7, max = 7, message = "Color must be 6 characters long.")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be in hex format.")
    private String color;

    public PokemonType toPokemonType() {
        PokemonType pokemonType = new PokemonType();
        pokemonType.setName(name);
        pokemonType.setColor(color);
        return pokemonType;
    }
}
