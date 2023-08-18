package obss.pokedex.pokemon.exception;

public class ServiceException extends RuntimeException {
    private ServiceException(String message) {
        super(message);
    }

    public static ServiceException PokemonWithNameNotFound(String pokemonName) {
        return new ServiceException(String.format("A Pokemon with the name %s was not found.", pokemonName));
    }

    public static ServiceException PokemonTypeWithNameNotFound(String pokemonTypeName) {
        return new ServiceException(String.format("A Pokemon type with the name %s was not found.", pokemonTypeName));
    }
}