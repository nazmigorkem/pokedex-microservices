package obss.pokedex.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import obss.pokedex.user.client.PokemonServiceClient;
import obss.pokedex.user.config.DataLoader;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.exception.ServiceException;
import obss.pokedex.user.model.*;
import obss.pokedex.user.model.kafka.UserListUpdate;
import obss.pokedex.user.repository.RoleRepository;
import obss.pokedex.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PokemonServiceClient pokemonServiceClient;
    private final KafkaTemplate<String, UserListUpdate> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PokemonServiceClient pokemonServiceClient, KafkaTemplate<String, UserListUpdate> kafkaTemplate, PasswordEncoder passwordEncoder, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.pokemonServiceClient = pokemonServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }


    public UserResponse addUser(UserAddRequest userAddRequest) {
        throwErrorIfUserExistsWithNameIgnoreCase(userAddRequest.getUsername());
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var roles = authentication.getAuthorities();
        var user = new User();
        user.setUsername(userAddRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userAddRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findByNameIgnoreCase(DataLoader.TRAINER_ROLE).orElseThrow()));
        if (userAddRequest.getRoles() != null && roles.stream().map(GrantedAuthority::getAuthority).anyMatch(t -> t.equals(DataLoader.ADMIN_ROLE))) {
            userAddRequest.getRoles().forEach(role -> {
                roleService.throwErrorIfRoleDoesNotExistWithNameIgnoreCase(role.getName());
                var roleEntity = roleRepository.findByNameIgnoreCase(role.getName()).orElseThrow();
                var userRoles = new HashSet<>(user.getRoles());
                userRoles.add(roleEntity);
                user.setRoles(userRoles);
            });
        }
        userRepository.save(user);
        return user.toUserResponse();
    }

    private void throwErrorIfUserExistsWithNameIgnoreCase(String username) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw ServiceException.userAlreadyExists(username);
        }
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
        userUpdateRequest.updateUser(user, passwordEncoder, roleRepository);
        userRepository.save(user);
        return user.toUserResponse();
    }

    public UserResponse addPokemonToUserWishList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (pokemon == null) {
            return user.toUserResponse();
        }
        user.addPokemonToWishList(pokemon);
        userRepository.save(user);
        kafkaTemplate.send("user-wish-list-addition", new UserListUpdate(pokemon.getId(), user.getId()));
        return user.toUserResponse();
    }

    public UserResponse deletePokemonFromUserWishList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (pokemon == null) {
            return user.toUserResponse();
        }
        user.removePokemonFromWishList(pokemon);
        userRepository.save(user);
        kafkaTemplate.send("user-wish-list-deletion", new UserListUpdate(pokemon.getId(), user.getId()));
        return user.toUserResponse();
    }

    public Page<PokemonResponse> getWishListByUsername(String username, int page, int size) {
        var uuids = userRepository.getWishListByUsernameIgnoreCase(username, PageRequest.of(page, size));
        return getPokemonResponses(page, size, uuids);
    }

    public UserResponse addPokemonToUserCatchList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (pokemon == null) {
            return user.toUserResponse();
        }
        user.addPokemonToCatchList(pokemon);
        userRepository.save(user);
        kafkaTemplate.send("user-catch-list-addition", new UserListUpdate(pokemon.getId(), user.getId()));
        return user.toUserResponse();
    }

    public UserResponse deletePokemonFromUserCatchList(UserPokemonRequest userPokemonRequest) {
        var user = userRepository.getUserByUsernameIgnoreCase(userPokemonRequest.getUsername()).orElseThrow();
        var pokemon = pokemonServiceClient.getPokemonByName(userPokemonRequest.getPokemonName()).getBody();
        if (pokemon == null) return user.toUserResponse();
        user.removePokemonFromCatchList(pokemon);
        userRepository.save(user);
        kafkaTemplate.send("user-catch-list-deletion", new UserListUpdate(pokemon.getId(), user.getId()));
        return user.toUserResponse();
    }

    public Page<PokemonResponse> getCatchListByUsername(String username, int page, int size) {
        var uuids = userRepository.getCatchListByUsernameIgnoreCase(username, PageRequest.of(page, size));
        return getPokemonResponses(page, size, uuids);
    }


    private Page<PokemonResponse> getPokemonResponses(int page, int size, Page<UUID> uuids) {
        if (uuids != null) {
            var pokemonResponses = pokemonServiceClient.getAllPokemonsByListQuery(uuids.getContent()).getBody();
            if (pokemonResponses != null) {
                return new PageImpl<>(pokemonResponses, PageRequest.of(page, size), uuids.getTotalElements());
            }
        }
        return Page.empty();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameIgnoreCase(username).orElseThrow();
        Hibernate.initialize(user.getRoles());
        return new MyUserDetails(user);
    }
}
