package obss.pokedex.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.validator.UpdateOptionalFields;
import obss.pokedex.user.validator.UsernameExistenceCheck;

@Builder
@Data
@UpdateOptionalFields(fields = {"newUsername", "newPassword"}, message = "At least one field should be filled.")
public class UserUpdateRequest {
    @NotBlank(message = "Search username cannot be blank.")
    @UsernameExistenceCheck(shouldExist = true, message = "Searched username does not exist.")
    private String searchUsername;

    @Size(min = 3, max = 20, message = "New username should be between 3 and 20 characters.")
    @UsernameExistenceCheck(shouldExist = false, message = "New username already exists.")
    private String newUsername;

    @Size(min = 3, max = 20, message = "New password should be between 3 and 20 characters.")
    private String newPassword;

    public void updateUser(User user) {
        if (newUsername != null) {
            user.setUsername(newUsername);
        }
        if (newPassword != null) {
            user.setPassword(newPassword);
        }
    }
}
