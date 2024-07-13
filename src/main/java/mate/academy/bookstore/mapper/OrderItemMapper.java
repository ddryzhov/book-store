package mate.academy.bookstore.mapper;

import java.util.Set;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    Set<OrderItemDto> map(Set<OrderItem> orderItems);
}
