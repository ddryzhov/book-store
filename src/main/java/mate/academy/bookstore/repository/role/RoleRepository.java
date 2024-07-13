package mate.academy.bookstore.repository.role;

import java.util.Optional;
import mate.academy.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleName roleName);
}
