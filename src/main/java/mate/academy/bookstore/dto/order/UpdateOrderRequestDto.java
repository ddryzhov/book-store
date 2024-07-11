package mate.academy.bookstore.dto.order;

import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.model.Order.Status;

@Getter
@Setter
public class UpdateOrderRequestDto {
    private Status status;
}
