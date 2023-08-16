package obss.pokedex.pokemon.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import obss.pokedex.pokemon.repository.PokemonTypeRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PokemonTypeShouldExistWithNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonTypeShouldExistWithName {
    String message() default "Pokemon with given name does not exist.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class PokemonTypeShouldExistWithNameValidator implements ConstraintValidator<PokemonTypeShouldExistWithName, String> {

    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonTypeShouldExistWithNameValidator(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null) {
            return true;
        }
        return pokemonTypeRepository.existsByName(name);
    }


    @Override
    public void initialize(PokemonTypeShouldExistWithName constraintAnnotation) {
    }
}