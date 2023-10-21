package it.psw.bookstore.user;

import it.psw.bookstore.exceptions.MailUserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return this.userService.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User savedUser = this.userService.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
    }

}
