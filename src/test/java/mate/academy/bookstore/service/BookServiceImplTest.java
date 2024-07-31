package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setIsbn("1111111111111");
        book.setPrice(new BigDecimal("12.9900"));
        book.setDescription("Book Description");
        book.setCoverImage("cover.jpg");

        bookDto = new BookDto()
                .setId(1L)
                .setTitle("Book Title")
                .setAuthor("Book Author")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("12.9900"))
                .setDescription("Book Description")
                .setCoverImage("cover.jpg");
    }

    @Test
    void save_ShouldSaveBook() {
        when(bookMapper.toEntity(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("3333333333333")
                .setPrice(new BigDecimal("12.9900"))
                .setDescription("Description")
                .setCoverImage("cover.jpg")
                .setCategoryIds(Set.of(1L));

        BookDto result = bookService.save(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void findAll_ShouldReturnAllBooks() {
        Pageable pageable = mock(Pageable.class);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        List<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findById_ShouldReturnBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        BookDto result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(1L));

        assertEquals("Can't find book by id 1", exception.getMessage());
    }

    @Test
    void deleteById_ShouldDeleteBook() {
        bookService.deleteById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void update_ShouldUpdateBook() {
        BookDto updatedBookDto = new BookDto()
                .setId(1L)
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("3333333333333")
                .setPrice(new BigDecimal("15.9900"))
                .setDescription("Updated Description")
                .setCoverImage("updated.jpg");

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(updatedBookDto);

        BookDto result = bookService.update(1L, updatedBookDto);

        assertNotNull(result);
        assertEquals("Updated Book", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void update_ShouldThrowException_WhenBookNotFound() {
        BookDto updatedBookDto = new BookDto();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(1L, updatedBookDto));

        assertEquals("Can't find book by id 1", exception.getMessage());
    }

    @Test
    void search_ShouldReturnBooks() {
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{"Dune"}, new String[]{"Frank Herbert"}
        );
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        Specification<Book> spec = Specification.where(null);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(spec);
        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                any(Pageable.class))
        ).thenReturn(bookPage);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        Pageable pageable = mock(Pageable.class);
        List<BookDto> result = bookService.search(searchParameters, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void findAllByCategoryId_ShouldReturnBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategoriesList(anyList())).thenReturn(List.of(
                new BookDtoWithoutCategoryIds(
                        1L, "Dune", "Frank Herbert",
                        "1111111111111", new BigDecimal("9.9900"),
                        "Science", "dune.jpg"
                )
        ));

        List<BookDtoWithoutCategoryIds> result = bookService.findAllByCategoryId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }
}
