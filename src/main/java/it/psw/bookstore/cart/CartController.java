package it.psw.bookstore.cart;

import it.psw.bookstore.exceptions.*;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.user.User;
import it.psw.bookstore.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam String email) throws UserNotFoundException {
        User user = this.userService.findByEmail(email);
        Cart cart = user.getCart();
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam String email,
                                       @RequestParam int bookId) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.addToCart(bookId, user);
            return new ResponseEntity<>("Book added successfully", HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }//addToCart

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam String email,
                                    @RequestParam int itemId) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.delete(itemId, user);
            return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
        } catch (CartDetailNotFoundException e) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }//delete

    @PutMapping
    public ResponseEntity<?> update(@RequestParam int itemId,
                                    @RequestParam String email,
                                    @RequestParam int newQuantity) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.update(itemId, newQuantity, user);
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (CartDetailNotFoundException e) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
    }//update

    @DeleteMapping("/clear")
    public ResponseEntity<?> clear(@RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.clear(user);
            return new ResponseEntity<>("Cart emptied successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }//clear

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            Order order = this.cartService.checkout(user);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OutdatedPriceException oe) {
            return new ResponseEntity<>("Product price not updated", HttpStatus.CONFLICT);
        } catch (NegativeQuantityException ne) {
            return new ResponseEntity<>("Unavailable quantity", HttpStatus.CONFLICT);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }//checkout

}
