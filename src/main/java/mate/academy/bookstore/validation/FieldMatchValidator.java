package mate.academy.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstValue = BeanUtils.getProperty(value, firstFieldName);
            Object secondValue = BeanUtils.getProperty(value, secondFieldName);

            return firstValue != null && firstValue.equals(secondValue);
        } catch (Exception e) {
            return false;
        }
    }
}
