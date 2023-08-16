package obss.pokedex.pokemon.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import obss.pokedex.pokemon.entity.PokemonType;
import obss.pokedex.pokemon.validator.PokemonTypeShouldExistWithName;
import obss.pokedex.pokemon.validator.PokemonTypeShouldNotExistWithName;
import obss.pokedex.pokemon.validator.UpdateOptionalFields;

@Data
@UpdateOptionalFields(fields = {"newName", "newColor"}, message = "Both new name and new color cannot be blank.")
public class PokemonTypeUpdateRequest {
    @NotBlank(message = "Search name cannot be blank.")
    @PokemonTypeShouldExistWithName(message = "Pokemon type does not exist with the given search name.")
    private String searchName;

    @PokemonTypeShouldNotExistWithName(message = "Pokemon type already exists with the given new name.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "New name must contain only alphanumeric characters.")
    private String newName;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be in hex format.")
    private String newColor;

    public PokemonType mapToPokemonType(PokemonType pokemonType) {
        pokemonType.setName(newName);
        if (newColor != null) {
            pokemonType.setColor(newColor);
        }
        return pokemonType;
    }
}
