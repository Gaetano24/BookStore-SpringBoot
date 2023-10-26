package it.psw.bookstore.book;

import it.psw.bookstore.support.exceptions.BookNotFoundException;
import it.psw.bookstore.support.exceptions.IsbnAlreadyExistsException;
import it.psw.bookstore.support.exceptions.NegativeQuantityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService implements BookServiceInterface {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(int id) throws BookNotFoundException {
        Optional<Book> opt = this.bookRepository.findById(id);
        if(opt.isEmpty()) {
            throw new BookNotFoundException();
        }
        return opt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Book> pagedResult = bookRepository.findAll(pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findByTitle(String title, int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Book> pagedResult = bookRepository.findByTitle(title, pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> advancedSearch(String title, String author, String publisher, String category,
                                     int pageNumber, int pageSize, String sortBy) {

        PageRequest pageable = switch (sortBy) {
            case "Titolo A-Z" -> PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            case "Titolo Z-A" -> PageRequest.of(pageNumber, pageSize, Sort.by("title").descending());
            case "Prezzo crescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
            case "Prezzo decrescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        };
        Page<Book> pagedResult = bookRepository.advancedSearch(title,author,publisher,category,pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Book save(Book book) throws IsbnAlreadyExistsException {
        if(this.bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IsbnAlreadyExistsException();
        }
        return this.bookRepository.save(book);
    }

    @Override
    @Transactional
    public void updatePrice(int id, float price) throws BookNotFoundException {
        Book book = findById(id);
        book.setPrice(price);
        this.bookRepository.save(book);
    }

    @Override
    @Transactional
    public void decreaseStock(int id, int quantity) throws BookNotFoundException, NegativeQuantityException {
        Book book = findById(id);
        int newQuantity = book.getQuantity()-quantity;
        if(newQuantity < 0) {
            throw new NegativeQuantityException();
        }
        book.setQuantity(newQuantity);
        this.bookRepository.save(book);
    }

}
