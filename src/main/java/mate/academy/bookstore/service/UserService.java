package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User register(UserRegistrationRequestDto request) throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email is already taken");
        }
        User user = userMapper.toEntity(request);
        return userRepository.save(user);
    }
}
