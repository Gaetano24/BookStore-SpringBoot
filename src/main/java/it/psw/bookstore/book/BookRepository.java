package it.psw.bookstore.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    boolean existsByIsbn(String isbn);
    Book findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%')))")
    Page<Book> findByTitle(@Param("title") String title, Pageable pageable);

    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL) AND " +
            "(LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')) OR :author IS NULL) AND " +
            "(LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%')) OR :publisher IS NULL) AND " +
            "(LOWER(b.category) LIKE LOWER(CONCAT('%', :category, '%')) OR :category IS NULL)")
    Page<Book> advancedSearch(@Param("title") String title,
                              @Param("author") String author,
                              @Param("publisher") String publisher,
                              @Param("category") String category,
                              Pageable pageable
    );

}
