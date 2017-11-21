package org.minijax.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.ValidationException;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;

import org.minijax.validator.builtin.AssertFalseValidator;
import org.minijax.validator.builtin.AssertTrueValidator;
import org.minijax.validator.builtin.IntegerMinValidator;
import org.minijax.validator.builtin.NotBlankValidator;
import org.minijax.validator.builtin.NotEmptyValidators.NotEmptyValidatorForArray;
import org.minijax.validator.builtin.NotEmptyValidators.NotEmptyValidatorForCharSequence;
import org.minijax.validator.builtin.NotEmptyValidators.NotEmptyValidatorForCollection;
import org.minijax.validator.builtin.NotEmptyValidators.NotEmptyValidatorForMap;
import org.minijax.validator.builtin.NotNullValidator;
import org.minijax.validator.builtin.PatternValidator;
import org.minijax.validator.builtin.SizeValidators.SizeValidatorForArray;
import org.minijax.validator.builtin.SizeValidators.SizeValidatorForCharSequence;
import org.minijax.validator.builtin.SizeValidators.SizeValidatorForCollection;
import org.minijax.validator.builtin.SizeValidators.SizeValidatorForMap;

public class MinijaxConstraintDescriptor<T extends Annotation> implements ConstraintDescriptor<T> {
    private final T annotation;
    private final ConstraintValidator<T, ?> validator;
    private final String messageTemplate;

    private MinijaxConstraintDescriptor(final T annotation, final ConstraintValidator<T, ?> validator) {
        this.annotation = annotation;
        this.validator = validator;
        messageTemplate = getMessageTemplate(annotation);
    }

    @SuppressWarnings("rawtypes")
    public ConstraintValidator getValidator() {
        validator.initialize(annotation);
        return validator;
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
    public static <T extends Annotation> MinijaxConstraintDescriptor<T> build(final AnnotatedType annotatedType, final T annotation) {
        final Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
        if (constraint == null) {
            return null;
        }

        final Class<?> valueClass = ReflectionUtils.getRawType(annotatedType);
        final Class<?> annotationClass = annotation.annotationType();

        if (constraint.validatedBy().length > 0) {
            return buildDeclaredValidator(annotation, constraint.validatedBy()[0]);

        } else if (annotationClass == AssertFalse.class) {
            return (MinijaxConstraintDescriptor<T>) buildAssertFalseValidator((AssertFalse) annotation, valueClass);

        } else if (annotationClass == AssertTrue.class) {
            return (MinijaxConstraintDescriptor<T>) buildAssertTrueValidator((AssertTrue) annotation, valueClass);

        } else if (annotationClass == Min.class) {
            return (MinijaxConstraintDescriptor<T>) buildMinValidator((Min) annotation, valueClass);

        } else if (annotationClass == NotBlank.class) {
            return (MinijaxConstraintDescriptor<T>) buildNotBlankValidator((NotBlank) annotation, valueClass);

        } else if (annotationClass == NotEmpty.class) {
            return (MinijaxConstraintDescriptor<T>) buildNotEmptyValidator((NotEmpty) annotation, valueClass);

        } else if (annotationClass == NotNull.class) {
            return (MinijaxConstraintDescriptor<T>) buildNotNullValidator((NotNull) annotation);

        } else if (annotationClass == Pattern.class) {
            return (MinijaxConstraintDescriptor<T>) buildPatternValidator((Pattern) annotation, valueClass);

        } else if (annotationClass == Size.class) {
            return (MinijaxConstraintDescriptor<T>) buildSizeValidator((Size) annotation, valueClass);

        } else {
            throw new ValidationException("Unrecognized constraint annotation: " + annotation);
        }
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T extends Annotation> MinijaxConstraintDescriptor<T> buildDeclaredValidator(final T annotation, final Class validatedBy) {
        final Class<? extends ConstraintValidator<T, ?>> c = validatedBy;
        try {
            return new MinijaxConstraintDescriptor<>(annotation, c.getConstructor().newInstance());
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }


    private static MinijaxConstraintDescriptor<AssertFalse> buildAssertFalseValidator(final AssertFalse assertFalse, final Class<?> valueClass) {
        if (valueClass == boolean.class || valueClass == Boolean.class) {
            return new MinijaxConstraintDescriptor<>(assertFalse, AssertFalseValidator.INSTANCE);
        }

        throw new ValidationException("Unsupported type for @AssertFalse annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<AssertTrue> buildAssertTrueValidator(final AssertTrue assertTrue, final Class<?> valueClass) {
        if (valueClass == boolean.class || valueClass == Boolean.class) {
            return new MinijaxConstraintDescriptor<>(assertTrue, AssertTrueValidator.INSTANCE);
        }

        throw new ValidationException("Unsupported type for @AssertTrue annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<Min> buildMinValidator(final Min min, final Class<?> valueClass) {
        if (valueClass == int.class || valueClass == Integer.class) {
            return new MinijaxConstraintDescriptor<>(min, new IntegerMinValidator(min));
        }

        throw new ValidationException("Unsupported type for @Min annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<NotBlank> buildNotBlankValidator(final NotBlank notBlank, final Class<?> valueClass) {
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(notBlank, NotBlankValidator.INSTANCE);
        }

        throw new ValidationException("Unsupported type for @NotBlank annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<NotEmpty> buildNotEmptyValidator(final NotEmpty notEmpty, final Class<?> valueClass) {
        if (valueClass.isArray()) {
            return new MinijaxConstraintDescriptor<>(notEmpty, NotEmptyValidatorForArray.INSTANCE);
        }

        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(notEmpty, NotEmptyValidatorForCharSequence.INSTANCE);
        }

        if (Collection.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(notEmpty, NotEmptyValidatorForCollection.INSTANCE);
        }

        if (Map.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(notEmpty, NotEmptyValidatorForMap.INSTANCE);
        }

        throw new ValidationException("Unsupported type for @NotEmpty annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<NotNull> buildNotNullValidator(final NotNull notNull) {
        return new MinijaxConstraintDescriptor<>(notNull, NotNullValidator.INSTANCE);
    }


    private static MinijaxConstraintDescriptor<Pattern> buildPatternValidator(final Pattern pattern, final Class<?> valueClass) {
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(pattern, new PatternValidator(pattern));
        }

        throw new ValidationException("Unsupported type for @Pattern annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<Size> buildSizeValidator(final Size size, final Class<?> valueClass) {
        if (valueClass.isArray()) {
            return new MinijaxConstraintDescriptor<>(size, new SizeValidatorForArray(size));
        }

        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(size, new SizeValidatorForCharSequence(size));
        }

        if (Collection.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(size, new SizeValidatorForCollection(size));
        }

        if (Map.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(size, new SizeValidatorForMap(size));
        }

        throw new ValidationException("Unsupported type for @Size annotation: " + valueClass);
    }


    private static String getMessageTemplate(final Annotation annotation) {
        try {
            return (String) annotation.annotationType().getMethod("message").invoke(annotation);
        } catch (final ReflectiveOperationException ex) {
            return null;
        }
    }
}
