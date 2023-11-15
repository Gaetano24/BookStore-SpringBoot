package it.psw.bookstore.cartDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    CartDetail findById(int id);

    CartDetail findByBookIdAndCartId(int bookId, int cartId);
}
