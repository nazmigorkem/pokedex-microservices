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
}