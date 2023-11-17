package it.psw.bookstore.cart;

import it.psw.bookstore.support.exceptions.*;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.user.User;
import jakarta.persistence.OptimisticLockException;

public interface CartServiceInterface {
    Cart getCart(User user);
    void addToCart(int bookId, User user) throws BookNotFoundException;
    void updateQuantity(int cartDetailId, int quantity, User user) throws ItemNotFoundException;
    void deleteItem(int cartDetailId, User user) throws ItemNotFoundException;
    void clear(User user);
    Order checkout(User user) throws OutdatedPriceException, NegativeQuantityException,
            OptimisticLockException, EmptyCartException;
}