package it.psw.bookstore.cart;

import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.support.exceptions.*;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.user.User;
import jakarta.persistence.OptimisticLockException;

import java.util.List;

public interface CartServiceInterface {
    void addToCart(int bookId, User user) throws BookNotFoundException;
    void updateItem(int cartDetailId, int quantity, User user) throws OutdatedCartException;
    void deleteItem(int cartDetailId, User user) throws OutdatedCartException;
    void clear(User user);
    Order checkout(User user) throws
            OutdatedPriceException, NegativeQuantityException,
            OptimisticLockException;
}