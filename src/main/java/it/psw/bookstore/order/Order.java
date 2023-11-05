package it.psw.bookstore.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.psw.bookstore.orderDetail.OrderDetail;
import it.psw.bookstore.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "total")
    @NotNull
    @PositiveOrZero
    private float total;

    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    public Order(User user) {
        this.user = user;
    }

}


