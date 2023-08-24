package obss.pokedex.user.exception;

public class ServiceException extends RuntimeException {
    private ServiceException(String message) {
        super(message);
    }

    public static ServiceException PlainError(String message) {
        return new ServiceException(message);
    }

    public static ServiceException PokemonAlreadyInWishList(String name) {
        return new ServiceException("Pokemon " + name + " is already in your wish list!");
    }

    public static ServiceException PokemonIsNotInWishList(String name) {
        return new ServiceException("Pokemon " + name + " is not in your wish list!");
    }

    public static ServiceException PokemonAlreadyInCatchList(String name) {
        return new ServiceException("Pokemon " + name + " is already in your catch list!");
    }

    public static ServiceException PokemonIsNotInCatchList(String name) {
        return new ServiceException("Pokemon " + name + " is not in your catch list!");
    }

    public static ServiceException UserAlreadyExists(String username) {
        return new ServiceException("User " + username + " already exists!");
    }
}