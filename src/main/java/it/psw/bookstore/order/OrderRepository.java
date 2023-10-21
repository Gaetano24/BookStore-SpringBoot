package it.psw.bookstore.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE o.user.email = :email ORDER BY o.createTime DESC")
    List<Order> findByCustomerEmail(String email);

}