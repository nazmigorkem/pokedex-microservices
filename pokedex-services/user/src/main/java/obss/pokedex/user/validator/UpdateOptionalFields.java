package obss.pokedex.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UpdateOptionalFieldsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateOptionalFields {
    String message() default "All fields cannot be null.";

    String[] fields();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class UpdateOptionalFieldsValidator implements ConstraintValidator<UpdateOptionalFields, Object> {

    private String[] fields;

    @Override
    public void initialize(UpdateOptionalFields constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        for (String field : fields) {
            Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
            if (fieldValue != null) {
                return true;
            }
        }
        return false;
    }
}