package it.psw.bookstore.order;

import it.psw.bookstore.support.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Order> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> pagedResult = this.orderRepository.findAllByOrderByCreateTimeDesc(pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(String email) {
        List<Order> orders = this.orderRepository.findByCustomerEmail(email);
        if(!orders.isEmpty()) {
            return orders;
        }
        return new ArrayList<>();
    }

}
