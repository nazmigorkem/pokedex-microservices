package obss.pokedex.user.repository;

import obss.pokedex.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Role findByNameIgnoreCase(String name);
}
