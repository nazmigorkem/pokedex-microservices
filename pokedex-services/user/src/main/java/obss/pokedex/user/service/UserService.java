package obss.pokedex.user.service;

import lombok.extern.slf4j.Slf4j;
import obss.pokedex.user.client.PokemonServiceClient;
import obss.pokedex.user.entity.User;
import obss.pokedex.user.model.*;
import obss.pokedex.user.model.kafka.UserListUpdate;
import obss.pokedex.user.model.keycloak.AccessTokenRequest;
import obss.pokedex.user.model.keycloak.AccessTokenResponse;
import obss.pokedex.user.model.keycloak.KeyCloakUserCreateRequest;
import obss.pokedex.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    public static final String ADMIN_CLIENT_URL = "http://localhost:8180/realms/master/protocol/openid-connect/token";
    public static final String USERS_URL = "http://localhost:8180/admin/realms/pokedex/users";
    private final UserRepository userRepository;
    private final PokemonServiceClient pokemonServiceClient;
    private final KafkaTemplate<String, UserListUpdate> kafkaTemplate;

    public UserService(UserRepository userRepository, PokemonServiceClient pokemonServiceClient, KafkaTemplate<String, UserListUpdate> kafkaTemplate) {
        this.userRepository = userRepository;
        this.pokemonServiceClient = pokemonServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    private static String getAdminCLIAccessToken() {
        var restTemplate = new RestTemplate();
        var adminClientHeaders = new HttpHeaders();
        adminClientHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> adminClientHttpEntity = new HttpEntity<>(AccessTokenRequest.getBody(), adminClientHeaders);
        var accessTokenResponse = restTemplate.postForEntity(ADMIN_CLIENT_URL, adminClientHttpEntity, AccessTokenResponse.class);
        return Objects.requireNonNull(accessTokenResponse.getBody()).getAccess_token();
    }

    public UserResponse addUser(UserAddRequest userAddRequest) {
        var user = userAddRequest.toUser();
        var accessToken = getAdminCLIAccessToken();
        var restTemplate = new RestTemplate();
        var userCreateRequestHeaders = new HttpHeaders();
        userCreateRequestHeaders.add("Authorization", "Bearer " + accessToken);
        userCreateRequestHeaders.add("Content-Type", "application/json");
        HttpEntity<KeyCloakUserCreateRequest> userCreateHttpEntity = new HttpEntity<>(userAddRequest.toKeycloakUser(), userCreateRequestHeaders);
        var response = restTemplate.postForEntity(USERS_URL, userCreateHttpEntity, Void.class);
        var keycloakUUID = Objects.requireNonNull(response.getHeaders().get("Location")).get(0).split(USERS_URL + "/")[1];
        user.setKeycloakId(UUID.fromString(keycloakUUID));
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
        userUpdateRequest.updateUser(user);
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
}
