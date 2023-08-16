package obss.pokedex.pokemon.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import obss.pokedex.pokemon.entity.PokemonType;
import obss.pokedex.pokemon.validator.PokemonTypeNameExistenceCheck;
import obss.pokedex.pokemon.validator.UpdateOptionalFields;

@Data
@UpdateOptionalFields(fields = {"newName", "newColor"}, message = "Both new name and new color cannot be blank.")
public class PokemonTypeUpdateRequest {
    @NotBlank(message = "Search name cannot be blank.")
    @PokemonTypeNameExistenceCheck(shouldExist = true, message = "Pokemon type does not exist with the given search name.")
    private String searchName;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "New name must contain only alphanumeric characters.")
    @Size(min = 3, max = 20, message = "New name must be between 3 and 20 characters long.")
    @PokemonTypeNameExistenceCheck(shouldExist = false, message = "Pokemon type already exists with the given new name.")
    private String newName;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be in hex format.")
    private String newColor;

    public void updatePokemonType(PokemonType pokemonType) {
        if (newColor != null) {
            pokemonType.setColor(newColor);
        }
        if (newName != null) {
            pokemonType.setName(newName);
        }
    }
}
