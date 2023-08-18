package obss.pokedex.user.repository;

import obss.pokedex.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsernameIgnoreCase(String name);

    Optional<User> getUserByUsernameIgnoreCase(String username);

    Page<User> getAllByUsernameStartsWithIgnoreCase(String username, Pageable pageable);

    @Query("select u.wishList from User u where u.username = ?1")
    Page<UUID> getWishListByUsernameIgnoreCase(String username, Pageable pageable);
}
