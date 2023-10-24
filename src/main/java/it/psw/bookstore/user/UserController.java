package it.psw.bookstore.user;

import it.psw.bookstore.exceptions.MailUserAlreadyExistsException;
import it.psw.bookstore.exceptions.UserNotFoundException;
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

    @GetMapping("admin/users")
    public List<User> getAll() {
        return this.userService.findAll();
    }

    @GetMapping("profile/{email}")
    public ResponseEntity<?> getProfile(@PathVariable("email") String email) {
        try {
            User user = this.userService.findByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
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