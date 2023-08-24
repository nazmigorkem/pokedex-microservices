package obss.pokedex.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.repository.RoleRepository;
import obss.pokedex.user.validator.AllRolesExistInListCheck;
import obss.pokedex.user.validator.UpdateOptionalFields;
import obss.pokedex.user.validator.UsernameExistenceCheck;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@UpdateOptionalFields(fields = {"newUsername", "newPassword", "newRoles"}, message = "At least one field should be filled.")
public class UserUpdateRequest {
    @NotBlank(message = "Search username cannot be blank.")
    @UsernameExistenceCheck(shouldExist = true, message = "Searched username does not exist.")
    private String searchUsername;
    @Size(min = 3, max = 20, message = "New username should be between 3 and 20 characters.")
    @UsernameExistenceCheck(shouldExist = false, message = "New username already exists.")
    private String newUsername;
    @Size(min = 3, max = 20, message = "New password should be between 3 and 20 characters.")
    private String newPassword;
    @Size(min = 1, message = "User should have at least 1 role.")
    @AllRolesExistInListCheck(shouldExist = true, message = "One of the roles does not exists.")
    private List<RoleResponse> newRoles;


    public void updateUser(User user, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        if (newUsername != null) {
            user.setUsername(newUsername);
        }
        if (newPassword != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        if (newRoles != null) {
            user.getRoles().forEach(x -> x.getUsers().remove(user));
            user.setRoles(newRoles.stream().map(x -> roleRepository.findByNameIgnoreCase(x.getName()).orElseThrow()).collect(Collectors.toSet()));
            user.getRoles().forEach(x -> x.getUsers().add(user));
        }
    }
}
