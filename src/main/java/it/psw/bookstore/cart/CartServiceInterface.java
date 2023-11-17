package it.psw.bookstore.cart;

import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.support.exceptions.*;
import it.psw.bookstore.order.Order;
import jakarta.persistence.OptimisticLockException;
import java.util.LinkedList;

public interface CartServiceInterface {
    Cart getCart(String email) throws UserNotFoundException;
    void addToCart(int bookId, String email) throws BookNotFoundException, UserNotFoundException;
    void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                             UserNotFoundException;
    void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException;
    void clear(String email) throws UserNotFoundException;
    Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
            NegativeQuantityException,
            OptimisticLockException,
            EmptyCartException,
            OutdatedCartException, UserNotFoundException;
}