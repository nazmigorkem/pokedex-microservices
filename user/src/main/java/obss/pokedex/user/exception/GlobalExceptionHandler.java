package obss.pokedex.user.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERRORS_KEY = "errors";

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, List<String>>> handleAccessDeniedException(RuntimeException e) {
        log.error("Runtime exception occurred: {}", e.getMessage());
        var map = Map.of(ERRORS_KEY, List.of("Access denied."));
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, List<String>>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception class: {}", e.getClass());
        log.error("Runtime exception occurred: {}", e.getMessage());
        var map = Map.of(ERRORS_KEY, List.of("Unexpected error occurred."));
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, List<String>>> handleServiceException(ServiceException e) {
        var map = Map.of(ERRORS_KEY, List.of(e.getMessage()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation exception occurred");
        List<String> errorList = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return new ResponseEntity<>(Map.of(ERRORS_KEY, errorList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<CharBuffer> handleFeignExceptionBadRequest(FeignException.BadRequest e) {
        log.warn("Feign exception bad request occurred");
        return new ResponseEntity<>(StandardCharsets.UTF_8.decode(e.responseBody().orElseThrow()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, List<String>>> handleNoSuchElementException(NoSuchElementException e) {
        log.warn("No such element exception occurred");
        var map = Map.of(ERRORS_KEY, List.of("No such element."));
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, List<String>>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("Missing servlet request parameter exception occurred");
        var map = Map.of(ERRORS_KEY, List.of("Missing request parameter. Parameter {" + e.getParameterName() + "} required."));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}