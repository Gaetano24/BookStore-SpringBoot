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
import it.psw.bookstore.user.UserService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class CartService implements CartServiceInterface {
    private final BookService bookService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @Autowired
    public CartService(BookService bookService, CartDetailRepository cartDetailRepository,
                       OrderRepository orderRepository, BookRepository bookRepository,
                       OrderDetailRepository orderDetailRepository, UserService userService) {
        this.bookService = bookService;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Cart getCart(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            Book book = cd.getBook();
            cd.setPrice(book.getPrice());
            cd.setSubTotal(cd.getQuantity()*book.getPrice());
        }
        System.out.println(cart);
        return cart;
    }

    @Override
    @Transactional
    public void addToCart(int bookId, String email) throws BookNotFoundException, UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        Book book = this.bookService.findById(bookId);
        CartDetail cd = this.cartDetailRepository.findByBookIdAndCartId(bookId, cart.getId());
        if(cd == null) {
            cd = new CartDetail(cart, book);
        }
        else {
            int newQuantity = cd.getQuantity()+1;
            cd.setQuantity(newQuantity);
            cd.setPrice(book.getPrice());
            cd.setSubTotal(book.getPrice()*newQuantity);
        }
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                                    UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
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
    public void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    @Override
    @Transactional
    public void clear(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
                                                                                    NegativeQuantityException,
                                                                                    OptimisticLockException,
                                                                                    EmptyCartException,
                                                                                    OutdatedCartException,
                                                                                    UserNotFoundException {
        User user = this.userService.findByEmail(email);
        List<CartDetail> dbCartDetails = user.getCart().getCartDetails();
        if(dbCartDetails.size() != cartDetails.size()) {
            throw new OutdatedCartException();
        }
        if(cartDetails.isEmpty()) {
            throw new EmptyCartException();
        }

        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;
        for(CartDetail cd: cartDetails) {
            CartDetail item = this.cartDetailRepository.findById(cd.getId());
            if(item == null || item.getCart().getUser().getId() != user.getId()) {
                throw new OutdatedCartException();
            }
            if(item.getQuantity() != cd.getQuantity()) {
                throw new OutdatedCartException();
            }

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
        return savedOrder;
    }

}
