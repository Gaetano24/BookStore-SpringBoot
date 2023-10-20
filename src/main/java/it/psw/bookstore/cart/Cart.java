package it.psw.bookstore.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user")
    @MapsId
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "cart")
    private List<CartDetail> cartDetails;

}
