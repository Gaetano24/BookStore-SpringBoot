package it.psw.bookstore.order;

import it.psw.bookstore.exceptions.OrderNotFoundException;
import it.psw.bookstore.orderDetail.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/profile/orders")
    public ResponseEntity<?> getOrders(@RequestParam String email) {
        List<Order> orders = orderService.findByCustomer(email);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }//getOrders

}
