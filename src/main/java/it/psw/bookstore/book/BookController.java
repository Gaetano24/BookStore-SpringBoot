package it.psw.bookstore.book;

import it.psw.bookstore.support.exceptions.BookNotFoundException;
import it.psw.bookstore.support.exceptions.IsbnAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Book> books = this.bookService.findAll(pageNumber, pageSize, sortBy);
        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/title")
    public ResponseEntity<?> getByTitle(@RequestParam(value = "title") String title,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Book> books = this.bookService.findByTitle(title, pageNumber, pageSize, sortBy);
        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/advancedSearch")
    public ResponseEntity<?> advancedSearch(@RequestParam(value = "title", defaultValue = "") String title,
                                            @RequestParam(value = "author", defaultValue = "") String author,
                                            @RequestParam(value = "publisher", defaultValue = "") String publisher,
                                            @RequestParam(value = "category", defaultValue = "") String category,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Book> books = bookService.advancedSearch(title, author, publisher, category, pageNumber, pageSize, sortBy);
        if (books.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/admin/addBook")
    public ResponseEntity<?> save(@Valid @RequestBody Book book) {
        try {
            Book savedBook = this.bookService.save(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (IsbnAlreadyExistsException e) {
            return new ResponseEntity<>("ISBN already exists", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/admin/books/{id}/updatePrice")
    public ResponseEntity<?> updatePrice(@PathVariable int id, @RequestParam float newPrice) {
        try {
            bookService.updatePrice(id, newPrice);
            return new ResponseEntity<>("Price updated successfully", HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }

}
