package it.psw.bookstore.cart;

import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.support.authentication.JwtUtils;
import it.psw.bookstore.support.exceptions.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;

@RestController
@RequestMapping("/profile/cart")
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCartDetails(Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            Cart cart = this.cartService.getCart(email);
            return new ResponseEntity<>(cart.getCartDetails(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam int bookId,
                                       Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.addToCart(bookId, email);
            return new ResponseEntity<>("Book added successfully", HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable("itemId") int itemId,
                                            @RequestParam int quantity,
                                            Authentication authentication) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            this.cartService.updateQuantity(itemId, quantity, email);
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
            this.cartService.deleteItem(itemId, email);
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
            this.cartService.clear(email);
            return new ResponseEntity<>("Cart emptied successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication authentication,
                                      @Valid @RequestBody LinkedList<CartDetail> cartDetails) {
        try {
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            Order order = this.cartService.checkout(email, cartDetails);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OutdatedPriceException oe) {
            return new ResponseEntity<>("Product price not updated", HttpStatus.CONFLICT);
        } catch (NegativeQuantityException ne) {
            return new ResponseEntity<>("Unavailable quantity", HttpStatus.CONFLICT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (EmptyCartException e) {
            return new ResponseEntity<>("Cart can't be empty", HttpStatus.BAD_REQUEST);
        } catch (OutdatedCartException e) {
            return new ResponseEntity<>("Cart not updated", HttpStatus.CONFLICT);
        }
    }

}
