package obss.pokedex.user.controller;

import jakarta.validation.Valid;
import obss.pokedex.user.model.UserAddRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserAddRequest userAddRequest) {
        return ResponseEntity.ok(userService.addUser(userAddRequest));
    }

}
