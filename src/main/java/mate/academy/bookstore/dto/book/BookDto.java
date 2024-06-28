package mate.academy.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title can be at most 255 characters long")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 255, message = "Author can be at most 255 characters long")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Size(max = 13, message = "ISBN can be at most 13 characters long")
    private String isbn;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Size(max = 255, message = "Description can be at most 255 characters long")
    private String description;

    @NotBlank(message = "Cover image cannot be blank")
    @Size(max = 255, message = "Cover Image URL can be at most 255 characters long")
    private String coverImage;

    private Set<Long> categoryIds;
}
