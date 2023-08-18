package obss.pokedex.pokemon.service;

import obss.pokedex.pokemon.client.UserServiceClient;
import obss.pokedex.pokemon.entity.Pokemon;
import obss.pokedex.pokemon.exception.ServiceException;
import obss.pokedex.pokemon.model.PokemonAddRequest;
import obss.pokedex.pokemon.model.PokemonResponse;
import obss.pokedex.pokemon.model.PokemonUpdateRequest;
import obss.pokedex.pokemon.model.UserAddPokemonRequest;
import obss.pokedex.pokemon.repository.PokemonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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

    public void addUserToWishListed(UserAddPokemonRequest userAddPokemonRequest) {
        pokemonRepository.findByNameIgnoreCase(userAddPokemonRequest.getPokemonName()).ifPresent(pokemon -> {
            var user = userServiceClient.getUserByName(userAddPokemonRequest.getUsername()).getBody();
            if (pokemon.getWishListedUsers() == null) {
                pokemon.setWishListedUsers(new HashSet<>());
            }
            if (user == null) return;
            pokemon.getWishListedUsers().add(user.getId());
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
}
