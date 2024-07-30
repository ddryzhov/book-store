package mate.academy.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.category.CategoryDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setup(@Autowired DataSource dataSource) throws SQLException {
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/categories/add-categories.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/books/add-books.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/books/add-book-category.sql"
        );
    }

    @AfterEach
    void teardown(@Autowired DataSource dataSource) throws SQLException {
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/books/remove-book-category.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/categories/remove-categories.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/bookstore/database/books/remove-books.sql"
        );
    }

    private void executeSqlScript(DataSource dataSource, String scriptPath) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category with valid input")
    public void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryDto requestDto = new CategoryDto()
                .setName("Science")
                .setDescription("Science Test");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(requestDto, actual, "id", "name", "description");
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all categories")
    public void getAll_GivenCategoriesInCatalog_ReturnAllCategories() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto().setId(1L).setName("Science Fiction").setDescription("good"));
        expected.add(new CategoryDto().setId(2L).setName("Fantasy").setDescription("good"));
        expected.add(new CategoryDto().setId(3L).setName("Mystery").setDescription("good"));

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto[].class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get a category by its ID")
    public void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(1L)
                .setName("Science Fiction")
                .setDescription("good");

        MvcResult result = mockMvc.perform(get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get books by category ID")
    public void getBooksByCategoryId_ValidId_ReturnsBooks() throws Exception {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds(
                1L, "Dune", "Frank Herbert",
                "1111111111111", new BigDecimal("9.9900"),
                "Science", "dune.jpg")
        );

        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDtoWithoutCategoryIds[].class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
        Assertions.assertTrue(Arrays.asList(actual).containsAll(expected));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update a category by its ID")
    public void updateCategory_ValidId_UpdatesCategory() throws Exception {
        CategoryDto requestDto = new CategoryDto()
                .setName("Updated Science")
                .setDescription("Updated Description");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(requestDto.getName(), actual.getName());
        Assertions.assertEquals(requestDto.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a category by its ID")
    public void deleteCategory_ValidId_DeletesCategory() throws Exception {
        executeSqlScript(dataSource,
                "mate/academy/bookstore/database/books/remove-book-category.sql");

        mockMvc.perform(
                        delete("/categories/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}
