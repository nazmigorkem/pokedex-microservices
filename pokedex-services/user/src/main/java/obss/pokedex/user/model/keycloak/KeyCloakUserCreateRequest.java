package obss.pokedex.user.model.keycloak;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class KeyCloakUserCreateRequest {
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private List<KeyCloakCredentials> credentials;
}
