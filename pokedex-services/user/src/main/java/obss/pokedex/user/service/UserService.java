package obss.pokedex.user.service;

import obss.pokedex.user.client.PokemonServiceClient;
import obss.pokedex.user.config.DataLoader;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.exception.ServiceException;
import obss.pokedex.user.model.*;
import obss.pokedex.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private static void throwErrorIfPokemonExistsInWishList(User user, PokemonResponse pokemon) {
        if (user.getWishList().contains(pokemon.getId())) {
            throw ServiceException.PokemonAlreadyInWishList(pokemon.getName());
        }
    }

    private static void throwErrorIfPokemonDoesNotExistInWishList(User user, PokemonResponse pokemon) {
        if (user.getWishList() == null || !user.getWishList().contains(pokemon.getId())) {
            throw ServiceException.PokemonIsNotInWishList(pokemon.getName());
        }
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

    public UserResponse addPokemonToUserWishList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (user.getWishList() == null) {
            user.setWishList(new HashSet<>());
        }
        if (pokemon == null) return user.toUserResponse();
        throwErrorIfPokemonExistsInWishList(user, pokemon);
        user.getWishList().add(pokemon.getId());
        pokemonServiceClient.addUserToWishListed(userPokemonRequest);
        userRepository.save(user);
        return user.toUserResponse();
    }

    public UserResponse removePokemonToUserWishList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (pokemon == null) return user.toUserResponse();
        throwErrorIfPokemonDoesNotExistInWishList(user, pokemon);
        user.getWishList().remove(pokemon.getId());
        pokemonServiceClient.deleteUserToWishListed(userPokemonRequest);
        userRepository.save(user);
        return user.toUserResponse();
    }

    public Page<PokemonResponse> getWishListByUsername(String username, int page, int size) {
        var uuids = userRepository.getWishListByUsernameIgnoreCase(username, PageRequest.of(page, size));


        if (uuids != null) {
            var pokemonResponses = pokemonServiceClient.getAllPokemonsByListQuery(uuids.getContent()).getBody();
            if (pokemonResponses != null) {
                return new PageImpl<>(pokemonResponses, PageRequest.of(page, size), uuids.getTotalElements());
            }
        }
        return Page.empty();
    }
}
