package it.psw.bookstore.cart;

import it.psw.bookstore.order.Order;
import it.psw.bookstore.support.exceptions.BookNotFoundException;
import it.psw.bookstore.support.exceptions.NegativeQuantityException;
import it.psw.bookstore.support.exceptions.OutdatedPriceException;
import it.psw.bookstore.support.exceptions.UserNotFoundException;
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
    public ResponseEntity<?> getCart(@RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            Cart cart = this.cartService.getCart(user);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam int bookId,
                                       @RequestParam String email) {
        try {
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
                                        @RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.updateItem(itemId, quantity, user);
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") int itemId, @RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.deleteItem(itemId, user);
            return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clear(@RequestParam String email) {
        try {
            User user = this.userService.findByEmail(email);
            this.cartService.clear(user);
            return new ResponseEntity<>("Cart emptied successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

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
    }

}
