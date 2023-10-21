package it.psw.bookstore.order;

import it.psw.bookstore.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOne(int id) throws OrderNotFoundException {
        Optional<Order> opt = this.orderRepository.findById(id);
        if(opt.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return opt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(String email) {
        return this.orderRepository.findByCustomerEmail(email);
    }

}
