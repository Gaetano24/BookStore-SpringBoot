package it.psw.bookstore.orderDetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.psw.bookstore.book.Book;
import it.psw.bookstore.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "order_details")
@Data
public class OrderDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Book.class)
    @JoinColumn(name = "book")
    private Book book;

    @Column(name = "price")
    @NotNull
    private float price;

    @Column(name = "quantity")
    @Positive
    @NotNull
    private int quantity;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase")
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @Column(name = "subtotal")
    @NotNull
    @Positive
    private float subTotal;

}
