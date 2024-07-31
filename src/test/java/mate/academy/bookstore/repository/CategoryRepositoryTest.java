package mate.academy.bookstore.repository;

import java.util.List;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all categories")
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:mate/academy/bookstore/database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ValidCategoriesInDatabase_ReturnsAllCategories() {
        List<Category> actual = categoryRepository.findAll();
        Assertions.assertEquals(3, actual.size());
        Assertions.assertTrue(actual.stream().anyMatch(
                category -> category.getName().equals("Science Fiction"))
        );
        Assertions.assertTrue(actual.stream().anyMatch(
                category -> category.getName().equals("Fantasy"))
        );
        Assertions.assertTrue(actual.stream().anyMatch(
                category -> category.getName().equals("Mystery"))
        );
    }
}
