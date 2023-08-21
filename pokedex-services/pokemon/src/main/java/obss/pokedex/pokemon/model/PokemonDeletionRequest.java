package obss.pokedex.pokemon.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PokemonDeletionRequest {
    private UUID pokemonId;
    private List<UUID> catchListedUsers;
    private List<UUID> wishListedUsers;
}
