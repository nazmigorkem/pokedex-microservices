package obss.pokedex.user.repository;

import obss.pokedex.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsernameIgnoreCase(String name);

    User getUserByUsernameIgnoreCase(String username);

    Page<User> getAllByUsernameStartsWithIgnoreCase(String username, Pageable pageable);
}
