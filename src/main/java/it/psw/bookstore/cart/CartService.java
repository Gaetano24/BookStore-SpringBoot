package it.psw.bookstore.cart;

import it.psw.bookstore.book.Book;
import it.psw.bookstore.book.BookRepository;
import it.psw.bookstore.book.BookService;
import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.cartDetail.CartDetailRepository;
import it.psw.bookstore.support.exceptions.*;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.order.OrderRepository;
import it.psw.bookstore.orderDetail.OrderDetail;
import it.psw.bookstore.orderDetail.OrderDetailRepository;
import it.psw.bookstore.user.User;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService implements CartServiceInterface {
    private final BookService bookService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public CartService(BookService bookService, CartDetailRepository cartDetailRepository,
                       OrderRepository orderRepository, BookRepository bookRepository,
                       OrderDetailRepository orderDetailRepository) {

        this.bookService = bookService;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    @Transactional
    public void addToCart(int bookId, User user) throws BookNotFoundException {
        Book book = this.bookService.findById(bookId);
        Cart cart = user.getCart();
        boolean found = false;
        for(CartDetail cd: cart.getCartDetails()) {
            if(cd.getBook().getId() == bookId) {
                int newQuantity = cd.getQuantity()+1;
                cd.setQuantity(newQuantity);
                cd.setPrice(book.getPrice());
                cd.setSubTotal(book.getPrice()*newQuantity);
                found = true;
                break;
            }
        }
        if(!found) {
            CartDetail cd = new CartDetail(cart, book);
            this.cartDetailRepository.save(cd);
        }
    }

    @Override
    @Transactional
    public void updateItem(int cartDetailId, int quantity, User user) throws OutdatedCartException {
        Cart cart = user.getCart();
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        if(cd == null || cd.getCart().getId() != cart.getId()) {
            throw new OutdatedCartException();
        }
        Book book = cd.getBook();
        cd.setQuantity(quantity);
        cd.setPrice(book.getPrice());
        cd.setSubTotal(quantity*book.getPrice());
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void deleteItem(int cartDetailId, User user) throws OutdatedCartException {
        Cart cart = user.getCart();
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        if(cd == null || cd.getCart().getId() != cart.getId()) {
            throw new OutdatedCartException();
        }
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    @Override
    @Transactional
    public void clear(User user) {
        Cart cart = user.getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order checkout(User user) throws OutdatedPriceException, NegativeQuantityException,
                                            OptimisticLockException {

        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;

        Cart cart = user.getCart();
        for(CartDetail item: cart.getCartDetails()) {

            Book book = item.getBook();
            float currentPrice = book.getPrice();
            float priceInCart = item.getPrice();
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            int quantity = item.getQuantity();
            int newQuantity = book.getQuantity()-quantity;
            if(newQuantity < 0) {
                throw new NegativeQuantityException();
            }
            book.setQuantity(newQuantity);
            Book updatedBook = this.bookRepository.save(book);

            OrderDetail od = new OrderDetail(updatedBook,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);

            item.setCart(null);
            this.cartDetailRepository.delete(item);

            total += priceInCart*quantity;
        }
        savedOrder.setTotal(total);
        return this.orderRepository.save(savedOrder);
    }

}
