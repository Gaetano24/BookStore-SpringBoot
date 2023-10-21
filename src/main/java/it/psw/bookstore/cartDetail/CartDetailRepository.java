package it.psw.bookstore.cartDetail;

import it.psw.bookstore.cart.Cart;
import it.psw.bookstore.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {

    @Query("SELECT cd FROM CartDetail cd WHERE cd.id = :id AND cd.cart = :cart")
    CartDetail findByIdAndCart(int id, Cart cart);

}
