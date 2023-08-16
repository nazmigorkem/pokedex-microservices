package obss.pokedex.pokemon.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.entity.PokemonType;
import obss.pokedex.pokemon.service.PokemonTypeService;
import obss.pokedex.pokemon.validator.PokemonNameExistenceCheck;
import obss.pokedex.pokemon.validator.PokemonTypeNameExistenceCheck;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;

@Data
public class PokemonAddRequest {
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters long.")
    @PokemonNameExistenceCheck(shouldExist = false, message = "Pokemon already exists with the given name.")
    private String name;

    @NotBlank(message = "First type cannot be blank.")
    @PokemonTypeNameExistenceCheck(shouldExist = true, message = "Pokemon type does not exist with the given first type.")
    private String firstType;

    @PokemonTypeNameExistenceCheck(shouldExist = true, message = "Pokemon type does not exist with the given second type.")
    private String secondType;

    @NotNull(message = "Attack cannot be blank.")
    @Max(value = 100, message = "Attack cannot be greater than 100.")
    @Min(value = 1, message = "Attack cannot be less than 1.")
    private Integer attack;

    @NotNull(message = "Defense cannot be blank.")
    @Max(value = 100, message = "Defense cannot be greater than 100.")
    @Min(value = 1, message = "Defense cannot be less than 1.")
    private Integer defense;

    @NotNull(message = "Health cannot be blank.")
    @Max(value = 100, message = "Health cannot be greater than 100.")
    @Min(value = 1, message = "Health cannot be less than 1.")
    private Integer health;

    @NotNull(message = "Special attack cannot be blank.")
    @Max(value = 100, message = "Special attack cannot be greater than 100.")
    @Min(value = 1, message = "Special attack cannot be less than 1.")
    private Integer specialAttack;

    @NotNull(message = "Special defense cannot be blank.")
    @Max(value = 100, message = "Special defense cannot be greater than 100.")
    @Min(value = 1, message = "Special defense cannot be less than 1.")
    private Integer specialDefense;

    @NotNull(message = "Speed cannot be blank.")
    @Max(value = 100, message = "Speed cannot be greater than 100.")
    @Min(value = 1, message = "Speed cannot be less than 1.")
    private Integer speed;

    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters long.")
    private String description;

    @NotBlank(message = "Image URL cannot be blank.")
    @URL(message = "Image url must be a valid url and it should be URL of https://assets.pokemon.com ", host = "assets.pokemon.com", protocol = "https")
    private String imageUrl;

    public Pokemon toPokemon(PokemonTypeService pokemonTypeService) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        Set<PokemonType> types = new HashSet<>();
        types.add(pokemonTypeService.getPokemonTypeEntityByName(firstType));
        if (secondType != null) {
            types.add(pokemonTypeService.getPokemonTypeEntityByName(secondType));
        }
        pokemon.setTypes(types);
        pokemon.setAttack(attack);
        pokemon.setDefense(defense);
        pokemon.setHealth(health);
        pokemon.setSpecialAttack(specialAttack);
        pokemon.setSpecialDefense(specialDefense);
        pokemon.setSpeed(speed);
        pokemon.setDescription(description);
        pokemon.setImageUrl(imageUrl);
        return pokemon;
    }
}
