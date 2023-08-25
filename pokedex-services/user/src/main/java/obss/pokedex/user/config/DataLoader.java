package obss.pokedex.user.config;

import lombok.extern.slf4j.Slf4j;
import obss.pokedex.user.entity.Role;
import obss.pokedex.user.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DataLoader implements ApplicationRunner {
    public static final String TRAINER_ROLE = "ROLE_TRAINER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DataLoader is running");

        var userRoleExists = roleRepository.existsByNameIgnoreCase(TRAINER_ROLE);
        if (!userRoleExists) {
            var userRole = new Role();
            userRole.setName(TRAINER_ROLE);
            roleRepository.save(userRole);
        }

        var adminRoleExists = roleRepository.existsByNameIgnoreCase(ADMIN_ROLE);
        if (!adminRoleExists) {
            var adminRole = new Role();
            adminRole.setName(ADMIN_ROLE);
            roleRepository.save(adminRole);
        }
    }
}
