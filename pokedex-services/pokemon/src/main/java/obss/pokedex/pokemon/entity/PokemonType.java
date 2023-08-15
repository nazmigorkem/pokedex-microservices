package obss.pokedex.pokemon.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pokemon_type")
public class PokemonType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
}
