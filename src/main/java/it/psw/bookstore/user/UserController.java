package it.psw.bookstore.user;

import it.psw.bookstore.support.RegistrationRequest;
import it.psw.bookstore.support.authentication.JwtUtils;
import it.psw.bookstore.support.exceptions.KeycloackRegistrationException;
import it.psw.bookstore.support.exceptions.MailUserAlreadyExistsException;
import it.psw.bookstore.support.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public List<User> getAll() {
        return this.userService.findAll();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            User savedUser = this.userService.register(registrationRequest);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        } catch (KeycloackRegistrationException e) {
            return new ResponseEntity<>("Error in registration", HttpStatus.BAD_REQUEST);
        }
    }

}