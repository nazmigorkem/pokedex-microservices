package obss.pokedex.user.controller;


import jakarta.validation.Valid;
import obss.pokedex.user.model.PokemonResponse;
import obss.pokedex.user.model.UserPokemonRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.service.UserService;
import obss.pokedex.user.validator.UsernameExistenceCheck;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list/catch")
public class CatchListController {

    private final UserService userService;

    public CatchListController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addPokemonToCatchList(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        return ResponseEntity.ok(userService.addPokemonToUserCatchList(userPokemonRequest));
    }

    @PostMapping("/delete")
    public ResponseEntity<UserResponse> removePokemonFromCatchList(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        return ResponseEntity.ok(userService.deletePokemonFromUserCatchList(userPokemonRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<Page<PokemonResponse>> getCatchListByUsername(
            @PathVariable @UsernameExistenceCheck(shouldExist = true, message = "User does not exists with this name.") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getCatchListByUsername(username, page, size));
    }
}
