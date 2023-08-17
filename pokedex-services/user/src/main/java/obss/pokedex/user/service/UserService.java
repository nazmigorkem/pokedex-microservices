package obss.pokedex.user.service;

import obss.pokedex.user.config.DataLoader;
import obss.pokedex.user.model.UserAddRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public UserResponse addUser(UserAddRequest userAddRequest) {
        var user = userAddRequest.toUser();
        user.setRoles(new HashSet<>());
        user.getRoles().add(roleService.getRoleEntityByNameIgnoreCase(DataLoader.TRAINER_ROLE));
        return userRepository.save(user).toUserResponse();
    }
}
