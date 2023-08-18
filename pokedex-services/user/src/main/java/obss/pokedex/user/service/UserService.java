package obss.pokedex.user.service;

import obss.pokedex.user.client.PokemonServiceClient;
import obss.pokedex.user.config.DataLoader;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.exception.ServiceException;
import obss.pokedex.user.model.UserAddPokemonRequest;
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
    private final PokemonServiceClient pokemonServiceClient;

    public UserService(UserRepository userRepository, RoleService roleService, PokemonServiceClient pokemonServiceClient) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.pokemonServiceClient = pokemonServiceClient;
    }

    public UserResponse addUser(UserAddRequest userAddRequest) {
        var user = userAddRequest.toUser();
        user.setRoles(new HashSet<>());
        user.getRoles().add(roleService.getRoleEntityByName(DataLoader.TRAINER_ROLE));
        return userRepository.save(user).toUserResponse();
    }

    public UserResponse getUserByName(String username) {
        return userRepository.getUserByUsernameIgnoreCase(username).orElseThrow().toUserResponse();
    }

    public Page<UserResponse> getUserPageStartsWithName(String username, int page, int size) {
        return userRepository.getAllByUsernameStartsWithIgnoreCase(username, PageRequest.of(page, size)).map(User::toUserResponse);
    }

    public void deleteUserByName(String username) {
        userRepository.delete(userRepository.getUserByUsernameIgnoreCase(username).orElseThrow());
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userUpdateRequest.getSearchUsername()).orElseThrow();
        var roles = userUpdateRequest.getNewRoles();
        if (roles != null) {
            user.setRoles(new HashSet<>());
            roles.forEach(role -> user.getRoles().add(roleService.getRoleEntityByName(role)));
        }
        userUpdateRequest.updateUser(user);
        userRepository.save(user);
        return user.toUserResponse();
    }

    public UserResponse addPokemonToUserWishList(UserAddPokemonRequest userAddPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userAddPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userAddPokemonRequest.getPokemonName()).getBody();
        if (user.getWishList() == null) {
            user.setWishList(new HashSet<>());
        }
        if (pokemon == null) return user.toUserResponse();
        if (user.getWishList().contains(pokemon.getId())) {
            throw ServiceException.PokemonAlreadyInWishList(pokemon.getName());
        }
        user.getWishList().add(pokemon.getId());
        pokemonServiceClient.addUserToWishListed(userAddPokemonRequest);
        userRepository.save(user);
        return user.toUserResponse();
    }
}
