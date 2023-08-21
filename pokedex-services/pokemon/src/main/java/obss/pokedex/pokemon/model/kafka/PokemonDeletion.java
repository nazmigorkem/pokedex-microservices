package obss.pokedex.pokemon.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PokemonDeletion {
    private UUID pokemonId;
    private List<UUID> catchListedUsers;
    private List<UUID> wishListedUsers;
}
