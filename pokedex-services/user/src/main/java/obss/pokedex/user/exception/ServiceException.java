package obss.pokedex.user.exception;

public class ServiceException extends RuntimeException {
    private ServiceException(String message) {
        super(message);
    }

    public static ServiceException PlainError(String message) {
        return new ServiceException(message);
    }
}