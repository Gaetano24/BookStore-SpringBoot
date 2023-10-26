package it.psw.bookstore.order;

import it.psw.bookstore.support.exceptions.OrderNotFoundException;
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

    @GetMapping("admin/orders")
    public ResponseEntity<?> getOrders(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<Order> orders = orderService.findAll(pageNumber, pageSize);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No orders found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("profile/orders")
    public ResponseEntity<?> getUserOrders(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam String email) {

        List<Order> orders = orderService.findByCustomer(email, pageNumber, pageSize);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No orders found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("profile/orders/{id}")
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
