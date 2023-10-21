package it.psw.bookstore.order;

import it.psw.bookstore.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderServiceInterface {
    Order findOne(int id) throws OrderNotFoundException;
    List<Order> findByCustomer(String email);

}
