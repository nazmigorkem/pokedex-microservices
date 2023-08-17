package obss.pokedex.user.controller;

import jakarta.validation.Valid;
import obss.pokedex.user.model.UserAddRequest;
import obss.pokedex.user.model.UserResponse;
import obss.pokedex.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{username}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByName(username));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> getUserPageStartsWithName(@RequestParam(defaultValue = "") String username,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getUserPageStartsWithName(username, page, size));
    }


}
