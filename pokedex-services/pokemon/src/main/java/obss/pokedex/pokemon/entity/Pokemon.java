package obss.pokedex.pokemon.entity;

import jakarta.persistence.*;
import lombok.Data;
import obss.pokedex.pokemon.model.PokemonResponse;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private Integer health;

    @Column(nullable = false)
    private Integer attack;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer specialAttack;

    @Column(nullable = false)
    private Integer specialDefense;

    @Column(nullable = false)
    private Integer defense;

    @Column(nullable = false)
    private Integer speed;

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

    public PokemonResponse toPokemonResponse() {
        return PokemonResponse.builder()
                .name(name)
                .types(types.stream().map(PokemonType::toPokemonTypeResponse).collect(Collectors.toSet()))
                .attack(attack)
                .defense(defense)
                .health(health)
                .specialAttack(specialAttack)
                .specialDefense(specialDefense)
                .speed(speed)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }
}
