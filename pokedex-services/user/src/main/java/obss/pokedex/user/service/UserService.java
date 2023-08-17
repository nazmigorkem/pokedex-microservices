package obss.pokedex.user.service;

import obss.pokedex.user.config.DataLoader;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.model.UserAddRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.model.UserUpdateRequest;
import obss.pokedex.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        user.getRoles().add(roleService.getRoleEntityByName(DataLoader.TRAINER_ROLE));
        return userRepository.save(user).toUserResponse();
    }

    public UserResponse getUserByName(String username) {
        return userRepository.getUserByUsernameIgnoreCase(username).toUserResponse();
    }

    public Page<UserResponse> getUserPageStartsWithName(String username, int page, int size) {
        return userRepository.getAllByUsernameStartsWithIgnoreCase(username, PageRequest.of(page, size)).map(User::toUserResponse);
    }

    public void deleteUserByName(String username) {
        userRepository.delete(userRepository.getUserByUsernameIgnoreCase(username));
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userUpdateRequest.getSearchUsername());
        userUpdateRequest.updateUser(user);
        userRepository.save(user);
        return user.toUserResponse();
    }
}
