package it.psw.bookstore.cart;

import it.psw.bookstore.book.Book;
import it.psw.bookstore.book.BookRepository;
import it.psw.bookstore.book.BookService;
import it.psw.bookstore.cartDetail.CartDetail;
import it.psw.bookstore.cartDetail.CartDetailRepository;
import it.psw.bookstore.exceptions.BookNotFoundException;
import it.psw.bookstore.exceptions.CartDetailNotFoundException;
import it.psw.bookstore.exceptions.NegativeQuantityException;
import it.psw.bookstore.exceptions.OutdatedPriceException;
import it.psw.bookstore.order.Order;
import it.psw.bookstore.order.OrderRepository;
import it.psw.bookstore.orderDetail.OrderDetail;
import it.psw.bookstore.orderDetail.OrderDetailRepository;
import it.psw.bookstore.user.User;
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
    @Transactional(readOnly = true)
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void addToCart(int bookId, User user) throws BookNotFoundException {
        Cart cart = user.getCart();
        Book book = this.bookService.findById(bookId);
        CartDetail cd = new CartDetail(cart,book);
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void delete(int cartDetailId, User user) throws CartDetailNotFoundException {
        Cart cart = user.getCart();
        CartDetail cd = this.cartDetailRepository.findByIdAndCart(cartDetailId, cart);
        if(cd == null) {
            throw new CartDetailNotFoundException();
        }
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    @Override
    @Transactional
    public void update(int cartDetailId, int quantity, User user) throws CartDetailNotFoundException {
        Cart cart = user.getCart();
        CartDetail cd = this.cartDetailRepository.findByIdAndCart(cartDetailId, cart);
        if(cd == null) {
            throw new CartDetailNotFoundException();
        }
        cd.setQuantity(quantity);
        cd.setSubTotal(cd.getPrice()*quantity);
        this.cartDetailRepository.save(cd);
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
    public Order checkout(User user) throws OutdatedPriceException, NegativeQuantityException, BookNotFoundException {
        //Creo il nuovo ordine
        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;

        //Effettuo i controlli necessari sugli articoli presenti nel carrello
        for(CartDetail cd: user.getCart().getCartDetails()) {
            //Prendo l'ultimo aggiornamento del libro
            String isbn = cd.getBook().getIsbn();
            Book book = this.bookRepository.findByIsbn(isbn);

            //Verifico che non ci siano state variazioni di prezzo
            float currentPrice = book.getPrice();
            float priceInCart = cd.getPrice();
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            //Aggiorno la disponibilità del prodotto gestendo eventuali insufficienze di scorte
            int quantity = cd.getQuantity();
            this.bookService.decreaseStock(book.getId(),quantity);
            Book updatedBook = this.bookRepository.save(book);

            //Creo il nuovo dettaglio ordine
            OrderDetail od = new OrderDetail(updatedBook,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);

            //Elimino dal carrello l'articolo
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);

            //Aggiorno il totale dell'ordine
            total += priceInCart*quantity;
        }
        savedOrder.setTotal(total);
        return savedOrder;
    }

}
