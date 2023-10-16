package obss.pokedex.user.exception;

public class ServiceException extends RuntimeException {

    public static final String POKEMON_SENTENCE_PREFIX = "Pokemon ";

    private ServiceException(String message) {
        super(message);
    }

    public static ServiceException plainError(String message) {
        return new ServiceException(message);
    }

    public static ServiceException pokemonAlreadyInWishList(String name) {
        return new ServiceException(POKEMON_SENTENCE_PREFIX + name + " is already in your wish list!");
    }

    public static ServiceException pokemonIsNotInWishList(String name) {
        return new ServiceException(POKEMON_SENTENCE_PREFIX + name + " is not in your wish list!");
    }

    public static ServiceException pokemonAlreadyInCatchList(String name) {
        return new ServiceException(POKEMON_SENTENCE_PREFIX + name + " is already in your catch list!");
    }

    public static ServiceException pokemonIsNotInCatchList(String name) {
        return new ServiceException(POKEMON_SENTENCE_PREFIX + name + " is not in your catch list!");
    }

    public static ServiceException userAlreadyExists(String username) {
        return new ServiceException("User " + username + " already exists!");
    }
}