package it.psw.bookstore.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    Page<Order> findAllByOrderByCreateTimeDesc(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email ORDER BY o.createTime DESC")
    Page<Order> findByCustomerEmail(String email, Pageable pageable);

}