package it.psw.bookstore.cart;

import it.psw.bookstore.exceptions.BookNotFoundException;
import it.psw.bookstore.exceptions.CartDetailNotFoundException;
import it.psw.bookstore.exceptions.NegativeQuantityException;
import it.psw.bookstore.exceptions.OutdatedPriceException;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.user.User;

public interface CartServiceInterface {

    Cart getCart(User user);
    void addToCart(int bookId, User user) throws BookNotFoundException;
    void delete(int cartDetailId, User user) throws CartDetailNotFoundException;
    void update(int cartDetailId, int quantity, User user) throws CartDetailNotFoundException;
    void clear(User user);
    Order checkout(User user) throws OutdatedPriceException, NegativeQuantityException, BookNotFoundException;

}
