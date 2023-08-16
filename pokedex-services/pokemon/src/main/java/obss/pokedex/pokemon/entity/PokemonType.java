package obss.pokedex.pokemon.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import obss.pokedex.pokemon.model.PokemonTypeResponse;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
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
    private Set<Pokemon> pokemons;

    public PokemonTypeResponse toPokemonTypeResponse() {
        return PokemonTypeResponse.builder()
                .name(name)
                .color(color)
                .build();
    }

    @PreRemove
    public void onPreRemove() {
        for (Pokemon pokemon : pokemons) {
            pokemon.getTypes().remove(this);
        }
    }
}
