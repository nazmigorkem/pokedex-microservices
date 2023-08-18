package obss.pokedex.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import obss.pokedex.user.validator.UsernameExistenceCheck;

@Data
public class UserAddPokemonRequest {
    @UsernameExistenceCheck(shouldExist = true, message = "User does not exist with this name.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotBlank(message = "Pokemon name cannot be blank.")
    private String pokemonName;
}
