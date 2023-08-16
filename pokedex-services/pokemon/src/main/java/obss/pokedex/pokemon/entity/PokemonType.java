package obss.pokedex.pokemon.entity;


import jakarta.persistence.*;
import lombok.Data;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "pokemon_type")
public class PokemonType {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, length = 7)
    private String color;

    @ManyToMany(mappedBy = "types")
    private List<Pokemon> pokemons;

    public PokemonTypeResponse toPokemonTypeResponse() {
        return PokemonTypeResponse.builder()
                .name(name)
                .color(color)
                .build();
    }
}
