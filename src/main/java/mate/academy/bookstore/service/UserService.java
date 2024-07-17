package mate.academy.bookstore.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.role.RoleRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ShoppingCartServiceImpl shoppingCartServiceImpl;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegistrationRequestDto request) throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email is already taken");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role defaultRole = roleRepository.findByRole(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(defaultRole));
        user = userRepository.save(user);
        shoppingCartServiceImpl.createShoppingCart(user);
        return user;
    }
}
