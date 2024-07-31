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
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
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
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                "mate/academy/bookstore/database/books/add-books.sql"
        );
    }

    @AfterEach
    void teardown(@Autowired DataSource dataSource) throws SQLException {
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
    @DisplayName("Create a new book with valid input")
    public void createBook_ValidRequestDto_ShouldReturnCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("3333333333333")
                .setPrice(new BigDecimal("12.9900"))
                .setDescription("Description")
                .setCoverImage("cover.jpg");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(requestDto, actual, "id", "title", "author", "coverImage");
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all books with pagination")
    public void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("Dune").setAuthor("Frank Herbert")
                .setIsbn("1111111111111").setPrice(new BigDecimal("9.9900"))
                .setDescription("Science").setCoverImage("dune.jpg")
                .setCategoryIds(new HashSet<>()));
        expected.add(new BookDto().setId(2L).setTitle("The Hobbit").setAuthor("Tolkien")
                .setIsbn("2222222222222").setPrice(new BigDecimal("8.9900"))
                .setDescription("Fantasy").setCoverImage("hobbit.jpg")
                .setCategoryIds(new HashSet<>()));

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto[].class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get a book by its ID")
    public void getBookById_ValidId_ShouldReturnBook() throws Exception {
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("9.9900"))
                .setDescription("Science")
                .setCoverImage("dune.jpg")
                .setCategoryIds(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update a book by its ID")
    public void updateBook_ValidId_ShouldUpdateBook() throws Exception {
        BookDto requestDto = new BookDto()
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("3333333333333")
                .setPrice(new BigDecimal("15.9900"))
                .setDescription("Updated Description")
                .setCoverImage("updated.jpg");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(requestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(requestDto.getAuthor(), actual.getAuthor());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a book by its ID")
    public void deleteBook_ValidId_ShouldDeleteBook() throws Exception {
        mockMvc.perform(
                        delete("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Search books with parameters")
    public void searchBooks_ValidParameters_ShouldReturnBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("Dune").setAuthor("Frank Herbert")
                .setIsbn("1111111111111").setPrice(new BigDecimal("9.9900"))
                .setDescription("Science").setCoverImage("dune.jpg")
                .setCategoryIds(new HashSet<>()));

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", "Dune")
                        .param("authors", "Frank Herbert")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto[].class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }
}
