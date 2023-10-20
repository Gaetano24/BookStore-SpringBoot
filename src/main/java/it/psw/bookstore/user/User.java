package it.psw.bookstore.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.psw.bookstore.cart.Cart;
import it.psw.bookstore.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "email")
    @NaturalId
    @NotBlank
    @Email
    private String email;

    @Column(name = "address")
    @NotBlank
    private String address;

    @Column(name = "phone")
    @NotBlank
    private String phone;

    @OneToMany(targetEntity = Order.class, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Order> orders;

    @OneToOne(targetEntity = Cart.class, mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

}
