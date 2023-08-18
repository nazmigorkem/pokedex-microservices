package obss.pokedex.user.controller;

import jakarta.validation.Valid;
import obss.pokedex.user.model.UserAddPokemonRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list/wish")
public class WishListController {

    private final UserService userService;

    public WishListController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addPokemonToWishList(@Valid @RequestBody UserAddPokemonRequest userAddPokemonRequest) {
        return ResponseEntity.ok(userService.addPokemonToUserWishList(userAddPokemonRequest));
    }
}
