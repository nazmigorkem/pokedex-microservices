package obss.pokedex.pokemon.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PokemonTypeNameExistenceCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonTypeNameExistenceCheck {
    String message() default "";

    boolean shouldExist() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class PokemonTypeNameExistenceCheckValidator implements ConstraintValidator<PokemonTypeNameExistenceCheck, String> {

    private final PokemonTypeRepository pokemonTypeRepository;
    private boolean shouldExist;

    public PokemonTypeNameExistenceCheckValidator(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (shouldExist && (name == null || name.isEmpty()))
            return true;
        return pokemonTypeRepository.existsByNameIgnoreCase(name) == shouldExist;
    }

    @Override
    public void initialize(PokemonTypeNameExistenceCheck constraintAnnotation) {
        shouldExist = constraintAnnotation.shouldExist();
    }
}