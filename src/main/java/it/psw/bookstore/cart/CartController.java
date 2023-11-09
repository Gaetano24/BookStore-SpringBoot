package it.psw.bookstore.cart;

import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.support.authentication.JwtUtils;
import it.psw.bookstore.support.exceptions.*;
import it.psw.bookstore.user.User;
import it.psw.bookstore.user.UserService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/profile/cart")
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getCartDetails(Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            List<CartDetail> cartDetails = user.getCart().getCartDetails();
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam int bookId,
                                       Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            this.cartService.addToCart(bookId, user);
            return new ResponseEntity<>("Book added successfully", HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("itemId") int itemId,
                                        @RequestParam int quantity,
                                        Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            this.cartService.updateItem(itemId, quantity, user);
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (OutdatedCartException e) {
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") int itemId, Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            this.cartService.deleteItem(itemId, user);
            return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (OutdatedCartException e) {
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clear(Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            this.cartService.clear(user);
            return new ResponseEntity<>("Cart emptied successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            Order order = this.cartService.checkout(user);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OutdatedPriceException oe) {
            return new ResponseEntity<>("Product price not updated", HttpStatus.CONFLICT);
        } catch (NegativeQuantityException ne) {
            return new ResponseEntity<>("Unavailable quantity", HttpStatus.CONFLICT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
