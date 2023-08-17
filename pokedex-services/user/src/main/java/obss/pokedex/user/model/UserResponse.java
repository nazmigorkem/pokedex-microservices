package obss.pokedex.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class UserResponse {
    private String username;
    private Set<String> roles;
}
