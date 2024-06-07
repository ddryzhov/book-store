package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Store", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books with pagination and sorting")
    @GetMapping
    public List<BookDto> getAll(
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = {"title", "author"})
            Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Create a new book")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Delete a book by its ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Update a book by its ID")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable @Positive Long id,
                              @RequestBody @Valid BookDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @Operation(summary = "Search for books with parameters")
    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParameters searchParameters,
                                     @Parameter(description = "Pagination and sorting information")
                                     @PageableDefault(size = 10, sort = {"title", "author"})
                                     Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
