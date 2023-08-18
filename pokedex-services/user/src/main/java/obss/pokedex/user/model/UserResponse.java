package obss.pokedex.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class UserResponse {
    private String username;
    private Set<String> roles;
    private Set<UUID> wishList;
    private Set<UUID> catchList;
}
