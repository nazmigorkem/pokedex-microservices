package obss.pokedex.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.user.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.Set;

@Documented
@Constraint(validatedBy = AllRolesExistInListCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllRolesExistInListCheck {
    String message() default "Invalid role set. One of the roles does not exists.";

    boolean shouldExist() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class AllRolesExistInListCheckValidator implements ConstraintValidator<AllRolesExistInListCheck, Set<String>> {

    private final RoleRepository roleRepository;

    public AllRolesExistInListCheckValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isValid(Set<String> roleNames, ConstraintValidatorContext constraintValidatorContext) {
        if (roleNames == null) {
            return true;
        }
        return roleNames.stream().allMatch(roleRepository::existsByNameIgnoreCase);
    }

    @Override
    public void initialize(AllRolesExistInListCheck constraintAnnotation) {
    }
}