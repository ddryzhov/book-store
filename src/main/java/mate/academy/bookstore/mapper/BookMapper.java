package mate.academy.bookstore.mapper;

import java.util.List;
import java.util.stream.Collectors;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    void updateModelFromDto(BookDto dto, @MappingTarget Book entity);

    List<BookDto> map(List<Book> books);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }
}
