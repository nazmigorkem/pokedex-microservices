package obss.pokedex.pokemon.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.entity.PokemonType;
import obss.pokedex.pokemon.service.PokemonTypeService;
import obss.pokedex.pokemon.validator.PokemonNameExistenceCheck;
import obss.pokedex.pokemon.validator.PokemonTypeNameExistenceCheck;
import obss.pokedex.pokemon.validator.UpdateOptionalFields;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;

@Data
@UpdateOptionalFields(fields = {"newName", "newHealth", "newAttack", "newDescription", "newSpecialAttack", "newSpecialDefense", "newDefense", "newSpeed", "newImageUrl"}, message = "All fields cannot be blank.")
public class PokemonUpdateRequest {
    @NotBlank(message = "Search name cannot be blank.")
    @PokemonNameExistenceCheck(shouldExist = true, message = "Pokemon does not exist with the given search name.")
    private String searchName;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "New name must contain only alphanumeric characters.")
    @Size(min = 3, max = 20, message = "New name must be between 3 and 20 characters long.")
    @PokemonNameExistenceCheck(shouldExist = false, message = "Pokemon already exists with the given new name.")
    private String newName;

    @PokemonTypeNameExistenceCheck(shouldExist = true, message = "Pokemon type does not exist with the given new first type.")
    private String newFirstType;

    @PokemonTypeNameExistenceCheck(shouldExist = true, message = "Pokemon type does not exist with the given new second type.")
    private String newSecondType;

    @Max(value = 100, message = "Attack cannot be greater than 100.")
    @Min(value = 1, message = "Attack cannot be less than 1.")
    private Integer newHealth;

    @Max(value = 100, message = "Attack cannot be greater than 100.")
    @Min(value = 1, message = "Attack cannot be less than 1.")
    private Integer newAttack;

    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters long.")
    private String newDescription;

    @Max(value = 100, message = "Special attack cannot be greater than 100.")
    @Min(value = 1, message = "Special attack cannot be less than 1.")
    private Integer newSpecialAttack;

    @Max(value = 100, message = "Special defense cannot be greater than 100.")
    @Min(value = 1, message = "Special defense cannot be less than 1.")
    private Integer newSpecialDefense;

    @Max(value = 100, message = "Defense cannot be greater than 100.")
    @Min(value = 1, message = "Defense cannot be less than 1.")
    private Integer newDefense;

    @Max(value = 100, message = "Speed cannot be greater than 100.")
    @Min(value = 1, message = "Speed cannot be less than 1.")
    private Integer newSpeed;

    @URL(message = "Image url must be a valid url and it should be URL of https://assets.pokemon.com ", host = "assets.pokemon.com", protocol = "https")
    private String newImageUrl;

    public void updatePokemon(Pokemon pokemon, PokemonTypeService pokemonTypeService) {
        if (newName != null) {
            pokemon.setName(newName);
        }
        if (newHealth != null) {
            pokemon.setHealth(newHealth);
        }
        if (newAttack != null) {
            pokemon.setAttack(newAttack);
        }
        if (newDescription != null) {
            pokemon.setDescription(newDescription);
        }
        if (newSpecialAttack != null) {
            pokemon.setSpecialAttack(newSpecialAttack);
        }
        if (newSpecialDefense != null) {
            pokemon.setSpecialDefense(newSpecialDefense);
        }
        if (newDefense != null) {
            pokemon.setDefense(newDefense);
        }
        if (newSpeed != null) {
            pokemon.setSpeed(newSpeed);
        }
        if (newImageUrl != null) {
            pokemon.setImageUrl(newImageUrl);
        }

        Set<PokemonType> types = new HashSet<>();
        if (newFirstType != null || newSecondType != null) {
            if (newFirstType != null) {
                types.add(pokemonTypeService.getPokemonTypeEntityByName(newFirstType));
            }
            if (newSecondType != null) {
                types.add(pokemonTypeService.getPokemonTypeEntityByName(newSecondType));
            }
            pokemon.setTypes(types);
        }
    }
}
