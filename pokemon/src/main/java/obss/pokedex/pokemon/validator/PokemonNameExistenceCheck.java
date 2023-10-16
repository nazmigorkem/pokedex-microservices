package obss.pokedex.pokemon.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.pokemon.repository.PokemonRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PokemonNameExistenceCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonNameExistenceCheck {
    String message() default "";

    boolean shouldExist() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


@Component
class PokemonNameExistenceCheckValidator implements ConstraintValidator<PokemonNameExistenceCheck, String> {

    private final PokemonRepository pokemonRepository;
    private boolean shouldExist;

    public PokemonNameExistenceCheckValidator(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (shouldExist && (name == null || name.isEmpty()))
            return true;
        return pokemonRepository.existsByNameIgnoreCase(name) == shouldExist;
    }

    @Override
    public void initialize(PokemonNameExistenceCheck constraintAnnotation) {
        shouldExist = constraintAnnotation.shouldExist();
    }
}