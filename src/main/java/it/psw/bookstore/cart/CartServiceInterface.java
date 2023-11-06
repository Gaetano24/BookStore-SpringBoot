package it.psw.bookstore.cart;

import it.psw.bookstore.support.exceptions.BookNotFoundException;
import it.psw.bookstore.support.exceptions.ItemNotFoundException;
import it.psw.bookstore.support.exceptions.NegativeQuantityException;
import it.psw.bookstore.support.exceptions.OutdatedPriceException;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.user.User;

public interface CartServiceInterface {
    void addToCart(int bookId, User user) throws BookNotFoundException;
    void updateItem(int cartDetailId, int quantity, User user) throws ItemNotFoundException;
    void deleteItem(int cartDetailId, User user) throws ItemNotFoundException;
    void clear(User user);
    Order checkout(User user) throws OutdatedPriceException, NegativeQuantityException, BookNotFoundException;

}