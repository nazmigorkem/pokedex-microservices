package obss.pokedex.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.model.keycloak.KeyCloakCredentials;
import obss.pokedex.user.model.keycloak.KeyCloakUserCreateRequest;
import obss.pokedex.user.validator.UsernameExistenceCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class UserAddRequest {
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters.")
    @UsernameExistenceCheck(shouldExist = false, message = "Username already exists.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 3, max = 20, message = "Password should be between 3 and 20 characters.")
    private String password;

    private Set<RoleResponse> roles;

    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        return user;
    }

    public KeyCloakUserCreateRequest toKeycloakUser() {
        return KeyCloakUserCreateRequest.builder()
                .username(this.username)
                .enabled(true)
                .firstName(this.username)
                .lastName(this.username)
                .email(this.username + "@obss.com")
                .credentials(new ArrayList<>(List.of(KeyCloakCredentials.builder()
                        .type("password")
                        .value(this.password)
                        .temporary(false)
                        .build())))
                .build();
    }
}
