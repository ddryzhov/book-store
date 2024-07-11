package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDto {
    @NotNull
    @Size(min = 1, max = 255)
    private String shippingAddress;
}
