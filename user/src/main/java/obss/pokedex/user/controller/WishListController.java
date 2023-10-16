package obss.pokedex.user.controller;

import jakarta.validation.Valid;
import obss.pokedex.user.model.PokemonResponse;
import obss.pokedex.user.model.UserPokemonRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.service.UserService;
import obss.pokedex.user.validator.UsernameExistenceCheck;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list/wish")
public class WishListController {

    private final UserService userService;

    public WishListController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addPokemonToWishList(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        return ResponseEntity.ok(userService.addPokemonToUserWishList(userPokemonRequest));
    }

    @PostMapping("/delete")
    public ResponseEntity<UserResponse> removePokemonToWishList(@Valid @RequestBody UserPokemonRequest userPokemonRequest) {
        return ResponseEntity.ok(userService.deletePokemonFromUserWishList(userPokemonRequest));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Page<PokemonResponse>> getWishListByUsername(
            @PathVariable @UsernameExistenceCheck(shouldExist = true, message = "User does not exists with this name.") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getWishListByUsername(username, page, size));
    }
}
