package obss.pokedex.user.model.keycloak;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class KeyCloakUserCreateRequest {
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
}
