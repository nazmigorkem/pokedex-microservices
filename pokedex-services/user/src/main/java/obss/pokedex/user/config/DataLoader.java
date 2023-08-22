package obss.pokedex.user.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    public static final String TRAINER_ROLE = "ROLE_TRAINER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
