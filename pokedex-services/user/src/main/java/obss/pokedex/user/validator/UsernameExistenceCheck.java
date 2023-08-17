package obss.pokedex.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameExistenceCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameExistenceCheck {
    String message() default "";

    boolean shouldExist() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


@Component
class UsernameExistenceCheckValidator implements ConstraintValidator<UsernameExistenceCheck, String> {

    private final UserRepository userRepository;
    private boolean shouldExist;

    public UsernameExistenceCheckValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (shouldExist && (name == null || name.isEmpty()))
            return true;
        return userRepository.existsByUsernameIgnoreCase(name) == shouldExist;
    }

    @Override
    public void initialize(UsernameExistenceCheck constraintAnnotation) {
        shouldExist = constraintAnnotation.shouldExist();
    }
}