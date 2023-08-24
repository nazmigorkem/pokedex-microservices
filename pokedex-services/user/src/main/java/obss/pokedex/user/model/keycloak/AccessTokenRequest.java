package obss.pokedex.user.model.keycloak;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class AccessTokenRequest {
    private static final String grant_type = "client_credentials";
    private static final String client_id = "admin-cli";
    private static final String client_secret = "3r0w0QIiwbqhyfjp6IKerF2c6vXaBkBa";

    public static MultiValueMap<String, String> getBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        return body;
    }
}
