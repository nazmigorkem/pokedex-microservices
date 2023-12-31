package obss.pokedex.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    private UUID id;
    private String username;
}
