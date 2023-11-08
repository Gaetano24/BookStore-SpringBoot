package it.psw.bookstore.book;

import it.psw.bookstore.support.exceptions.BookNotFoundException;
import it.psw.bookstore.support.exceptions.IsbnAlreadyExistsException;

import java.util.List;

public interface BookServiceInterface {

    Book findById(int id) throws BookNotFoundException;
    List<Book> findAll(int pageNumber, int pageSize, String sortBy);
    List<Book> findByTitle(String title, int pageNumber, int pageSize, String sortBy);
    List<Book> advancedSearch(String title, String author, String publisher, String category,
                              int pageNumber, int pageSize, String sortBy);
    Book save(Book book) throws IsbnAlreadyExistsException;
    void updatePrice(int id, float price) throws BookNotFoundException;

}
