package it.psw.bookstore.order;

import it.psw.bookstore.exceptions.OrderNotFoundException;
import it.psw.bookstore.orderDetail.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("profile/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam int pageNumber,
                                       @RequestParam int pageSize,
                                       @RequestParam String email) {

        List<Order> orders = orderService.findByCustomer(email, pageNumber, pageSize);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No results found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("id") int id, @RequestParam String email) {
        try {
            Order order = this.orderService.findOne(id);
            if(!order.getUser().getEmail().equals(email)) {
                return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }
            List<OrderDetail> od = order.getOrderDetails();
            return new ResponseEntity<>(od, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

}
