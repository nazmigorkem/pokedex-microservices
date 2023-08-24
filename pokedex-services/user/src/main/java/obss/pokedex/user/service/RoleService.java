package obss.pokedex.user.service;

import obss.pokedex.user.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //*****************//
    //* GUARD CLAUSES *//
    //*************** *//

    public void throwErrorIfRoleDoesNotExistWithNameIgnoreCase(String name) {
        if (!roleRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Role with name " + name + " does not exist.");
        }
    }
}