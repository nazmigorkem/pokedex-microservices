package obss.pokedex.user.service;

import obss.pokedex.user.entity.Role;
import obss.pokedex.user.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleEntityByName(String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow();
    }
}
