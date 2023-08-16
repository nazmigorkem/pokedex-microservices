package obss.pokedex.pokemon.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PokemonTypeShouldNotExistWithNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonTypeShouldNotExistWithName {
    String message() default "Pokemon with given name already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class PokemonTypeShouldNotExistWithNameValidator implements ConstraintValidator<PokemonTypeShouldNotExistWithName, String> {

    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonTypeShouldNotExistWithNameValidator(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !pokemonTypeRepository.existsByName(name);
    }

    @Override
    public void initialize(PokemonTypeShouldNotExistWithName constraintAnnotation) {
    }
}