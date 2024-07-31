package mate.academy.bookstore.repository;

import java.util.List;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books")
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/add-book-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/remove-book-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/remove-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ValidBooksInDatabase_ReturnsAllBooks() {
        List<Book> actual = bookRepository.findAll();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertTrue(actual.stream().anyMatch(
                book -> book.getTitle().equals("Dune"))
        );
        Assertions.assertTrue(actual.stream().anyMatch(
                book -> book.getTitle().equals("The Hobbit"))
        );
    }

    @Test
    @DisplayName("Find all books by category ID")
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/add-book-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/remove-book-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/books/remove-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_CategoryIdOne_ReturnsBooksWithCategoryIdOne() {
        List<Book> actual = bookRepository.findAllByCategoryId(1L);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("Dune", actual.get(0).getTitle());
    }
}
