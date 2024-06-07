package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.validation.FieldMatch;

@Getter
@Setter
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords do not match")
public class UserRegistrationRequestDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Size(min = 8)
    private String repeatPassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String shippingAddress;
}
