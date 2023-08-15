package obss.pokedex.pokemon.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "pokemon")
public class Pokemon {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int health;

    @Column(nullable = false)
    private int attack;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int specialAttack;

    @Column(nullable = false)
    private int specialDefense;

    @Column(nullable = false)
    private int defense;

    @Column(nullable = false)
    private int speed;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToMany
    @JoinTable(name = "POKEMON_POKEMON_TYPE",
            joinColumns = @JoinColumn(name = "POKEMON_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID"))
    private Set<PokemonType> types;

    @ElementCollection
    private Set<UUID> wishListedUsers;

    @ElementCollection
    private Set<UUID> catchListedUsers;

    public void addType(PokemonType pokemonType) {
        types.add(pokemonType);
    }
}
