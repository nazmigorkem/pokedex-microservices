package obss.pokedex.pokemon_type.model;


import lombok.Builder;
import lombok.Data;
import obss.pokedex.pokemon_type.entity.PokemonType;

@Data
public class PokemonTypeAddRequest {
    private String name;
    private String color;

    public PokemonType toPokemonType() {
        PokemonType pokemonType = new PokemonType();
        pokemonType.setName(name);
        pokemonType.setColor(color);
        return pokemonType;
    }
}
