package it.psw.bookstore.order;

import java.util.List;

public interface OrderServiceInterface {
    Order findOne(int id) throws OrderNotFoundException;
    List<Order> findByCustomer(String email);

}
