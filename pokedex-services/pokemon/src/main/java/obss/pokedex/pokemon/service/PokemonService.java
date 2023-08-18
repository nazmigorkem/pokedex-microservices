package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.client.UserServiceClient;
import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.model.PokemonUpdateRequest;
import obss.pokedex.pokemon.model.UserPokemonRequest;
import obss.pokedex.pokemon.repository.PokemonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokemonTypeService pokemonTypeService;
    private final UserServiceClient userServiceClient;

    public PokemonService(PokemonRepository pokemonRepository, PokemonTypeService pokemonTypeService, UserServiceClient userServiceClient) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonTypeService = pokemonTypeService;
        this.userServiceClient = userServiceClient;
    }

    public PokemonResponse addPokemon(PokemonAddRequest pokemonAddRequest) {
        return pokemonRepository.save(pokemonAddRequest.toPokemon(pokemonTypeService)).toPokemonResponse();
    }

    public PokemonResponse updatePokemon(PokemonUpdateRequest pokemonUpdateRequest) {
        Pokemon pokemon = pokemonRepository.findByNameIgnoreCase(pokemonUpdateRequest.getSearchName()).orElseThrow();
        pokemonUpdateRequest.updatePokemon(pokemon, pokemonTypeService);
        return pokemonRepository.save(pokemon).toPokemonResponse();
    }

    public PokemonResponse getPokemonByName(String name) {
        throwServiceExceptionIfPokemonDoesNotExistWithName(name);
        return pokemonRepository.findByNameIgnoreCase(name).orElseThrow().toPokemonResponse();
    }

    public Page<PokemonResponse> getPokemonPageByStartsWithName(String name, int page, int size) {
        return pokemonRepository.findAllByNameStartsWithIgnoreCase(name, PageRequest.of(page, size)).map(Pokemon::toPokemonResponse);
    }

    public void deletePokemonByName(String name) {
        throwServiceExceptionIfPokemonDoesNotExistWithName(name);
        pokemonRepository.findByNameIgnoreCase(name).ifPresent(pokemonRepository::delete);
    }

    public void addUserToWishListed(UserPokemonRequest userPokemonRequest) {
        pokemonRepository.findByNameIgnoreCase(userPokemonRequest.getPokemonName()).ifPresent(pokemon -> {
            var user = userServiceClient.getUserByName(userPokemonRequest.getUsername()).getBody();
            if (pokemon.getWishListedUsers() == null) {
                pokemon.setWishListedUsers(new HashSet<>());
            }
            if (user == null) return;
            pokemon.getWishListedUsers().add(user.getId());
            pokemonRepository.save(pokemon);
        });
    }

    public void deleteUserToWishListed(UserPokemonRequest userPokemonRequest) {
        pokemonRepository.findByNameIgnoreCase(userPokemonRequest.getPokemonName()).ifPresent(pokemon -> {
            var user = userServiceClient.getUserByName(userPokemonRequest.getUsername()).getBody();
            if (pokemon.getWishListedUsers() == null) return;
            if (user == null) return;
            pokemon.getWishListedUsers().remove(user.getId());
            pokemonRepository.save(pokemon);
        });
    }

    /*
        GUARD CLAUSES
     */

    private void throwServiceExceptionIfPokemonDoesNotExistWithName(String name) {
        if (!pokemonRepository.existsByNameIgnoreCase(name))
            throw ServiceException.PokemonWithNameNotFound(name);
    }

    public List<PokemonResponse> getAllPokemonsByListQuery(List<UUID> uuids) {
        return pokemonRepository.findAllByIdIn(uuids).stream().map(Pokemon::toPokemonResponse).toList();
    }
}
