package it.psw.bookstore.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.orderDetail.OrderDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "books")
@Data
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "author")
    @NotBlank
    private String author;

    @Column(name = "publisher")
    @NotBlank
    private String publisher;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "isbn")
    @NaturalId
    @NotBlank
    @Size(min = 13, max = 13)
    private String isbn;

    @Column(name = "price")
    @NotNull
    @Positive
    private float price;

    @Column(name = "quantity")
    @NotNull
    @PositiveOrZero
    private int quantity;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<CartDetail> cartDetails;

}
