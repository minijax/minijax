package org.minijax.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;

import org.minijax.validator.builtin.CharSequencePatternValidator;
import org.minijax.validator.builtin.CharSequenceSizeValidator;
import org.minijax.validator.builtin.IntegerMinValidator;
import org.minijax.validator.builtin.NotNullValidator;

public class MinijaxConstraintDescriptor<T extends Annotation> implements ConstraintDescriptor<T> {
    private final T annotation;
    private final ConstraintValidator<T, ?> constraintValidator;
    private final String messageTemplate;

    private MinijaxConstraintDescriptor(final T annotation, final ConstraintValidator<T, ?> constraintValidator, final String messageTemplate) {
        this.annotation = annotation;
        this.constraintValidator = constraintValidator;
        this.messageTemplate = messageTemplate;
    }

    public ConstraintValidator<T, ?> getConstraintValidator() {
        return constraintValidator;
    }

    @Override
    public T getAnnotation() {
        return annotation;
    }

    @Override
    public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public Set<Class<?>> getGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Class<? extends Payload>> getPayload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintTarget getValidationAppliesTo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReportAsSingleViolation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidateUnwrappedValue getValueUnwrapping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U unwrap(final Class<U> type) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> MinijaxConstraintDescriptor<T> build(final AnnotatedType annotatedType, final T annotation)
            throws ReflectiveOperationException {

        final Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
        final Class<?> valueClass = (Class<?>) annotatedType.getType();
        final Class<?> annotationClass = annotation.annotationType();

        final List<Class<? extends ConstraintValidator<T, ?>>> declared =
                Arrays.asList((Class<? extends ConstraintValidator<T, ?>>[]) constraint.validatedBy());

        final ConstraintValidator<T, ?> constraintValidator;
        final String messageKey;

        if (!declared.isEmpty()) {
            constraintValidator = declared.get(0).getConstructor().newInstance();
            messageKey = "";

        } else if (annotationClass == Min.class) {
            final Min min = (Min) annotation;
            messageKey = min.message();

            if (valueClass == int.class || valueClass == Integer.class) {
                constraintValidator = (ConstraintValidator<T, ?>) new IntegerMinValidator(min);
            } else {
                throw new IllegalArgumentException("Unsupported type for @Min annotation: " + valueClass);
            }

        } else if (annotationClass == NotNull.class) {
            constraintValidator = (ConstraintValidator<T, ?>) NotNullValidator.getInstance();
            messageKey = ((NotNull) annotation).message();

        } else if (annotationClass == Pattern.class) {
            final Pattern pattern = (Pattern) annotation;
            messageKey = pattern.message();

            if (CharSequence.class.isAssignableFrom(valueClass)) {
                constraintValidator = (ConstraintValidator<T, ?>) new CharSequencePatternValidator(pattern);
            } else {
                throw new IllegalArgumentException("Unsupported type for @Pattern annotation: " + valueClass);
            }

        } else if (annotationClass == Size.class) {
            final Size size = (Size) annotation;
            messageKey = size.message();

            if (CharSequence.class.isAssignableFrom(valueClass)) {
                constraintValidator = (ConstraintValidator<T, ?>) new CharSequenceSizeValidator(size);
            } else {
                throw new IllegalArgumentException("Unsupported type for @Size annotation: " + valueClass);
            }

        } else {
            throw new IllegalArgumentException("Unrecognized constraint annotation: " + annotation);
        }

        final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.minijax.validator.ValidationMessages");
        final String messageTemplate = resourceBundle.getString(messageKey.substring(1, messageKey.length() - 1));
        return new MinijaxConstraintDescriptor<>(annotation, constraintValidator, messageTemplate);
    }
}
