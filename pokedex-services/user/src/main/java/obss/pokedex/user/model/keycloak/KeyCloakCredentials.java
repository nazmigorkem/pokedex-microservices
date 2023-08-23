package obss.pokedex.user.model.keycloak;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class KeyCloakCredentials {
    private String type;
    private String value;
    private boolean temporary;
}
