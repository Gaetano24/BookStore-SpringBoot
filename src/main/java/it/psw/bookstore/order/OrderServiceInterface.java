package it.psw.bookstore.order;

import it.psw.bookstore.support.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderServiceInterface {
    Order findOne(int id) throws OrderNotFoundException;
    List<Order> findAll(int pageNumber, int pageSize);
    List<Order> findByCustomer(String email);

}
