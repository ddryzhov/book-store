package mate.academy.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDto {
    private Long id;

    @NotBlank
    private String name;

    private String description;
}
