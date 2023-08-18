package obss.pokedex.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class PokemonResponse {
    private UUID id;
    private String name;
    private Set<PokemonTypeResponse> types;
    private Integer attack;
    private Integer defense;
    private Integer health;
    private Integer specialAttack;
    private Integer specialDefense;
    private Integer speed;
    private String description;
    private String imageUrl;
}
