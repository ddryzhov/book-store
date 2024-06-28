package mate.academy.bookstore.mapper;

import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
