package org.example;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class Validator {

    private Validator() {
    }

    public static ValidationResult validate(Object object) {
        if (object == null) {
            throw new NullPointerException("Объяект не может быть null");
        }

        List<ValidationError> errors = new ArrayList<>();

        Class<?> current = object.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                checkField(object, field, errors);
            }
            current = current.getSuperclass();
        }

        return new ValidationResult(errors);
    }

    private static void checkField(Object object, Field field, List<ValidationError> errors) {
        NotNull notNull = field.getAnnotation(NotNull.class);
        Size size = field.getAnnotation(Size.class);
        Range range = field.getAnnotation(Range.class);
        Email email = field.getAnnotation(Email.class);

        if (notNull == null && size == null && range == null && email == null) {
            return;
        }

        if (size != null) {
            checkSizeConfig(field, size);
        }
        if (range != null) {
            checkRangeConfig(field, range);
        }
        if (email != null && !CharSequence.class.isAssignableFrom(field.getType())) {
            throw unsupported(field, "Email");
        }

        Object value = readValue(object, field);
        String name = field.getName();

        if (notNull != null && value == null && !field.getType().isPrimitive()) {
            errors.add(new ValidationError(name, "NotNull", notNull.message()));
        }

        if (value == null) {
            return;
        }

        if (size != null) {
            int length = ((CharSequence) value).length();
            if (length < size.min() || length > size.max()) {
                errors.add(new ValidationError(name, "Size", size.message()));
            }
        }

        if (range != null) {
            double number = ((Number) value).doubleValue();
            if (Double.isNaN(number) || number < range.min() || number > range.max()) {
                errors.add(new ValidationError(name, "Range", range.message()));
            }
        }

        if (email != null && !isEmail(value.toString())) {
            errors.add(new ValidationError(name, "Email", email.message()));
        }
    }

    private static void checkSizeConfig(Field field, Size size) {
        if (!CharSequence.class.isAssignableFrom(field.getType())) {
            throw unsupported(field, "Size");
        }
        if (size.min() < 0 || size.min() > size.max()) {
            throw new ValidationConfigurationException("Некорректные границы у поля " + describe(field) + ": min = " + size.min() + ", max = " + size.max());
        }
    }

    private static void checkRangeConfig(Field field, Range range) {
        if (!isNumericType(field.getType())) {
            throw unsupported(field, "Range");
        }
        double min = range.min();
        double max = range.max();
        if (Double.isNaN(min) || Double.isNaN(max)
                || Double.isInfinite(min) || Double.isInfinite(max) || min > max) {
            throw new ValidationConfigurationException(
                    "Некорректные границы у поля " + describe(field) + ": min = " + min + ", max = " + max);
        }
    }

    private static boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || type == byte.class || type == short.class || type == int.class || type == long.class || type == float.class || type == double.class;
    }

    private static boolean isEmail(String text) {
        if (text.isBlank()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        int at = text.indexOf('@');
        if (at <= 0 || at != text.lastIndexOf('@') || at == text.length() - 1) {
            return false;
        }
        return text.substring(at + 1).contains(".");
    }

    private static Object readValue(Object object, Field field) {
        if (!field.trySetAccessible()) {
            throw new ValidationConfigurationException("Поле неозможно прочитать: " + describe(field));
        }
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ValidationConfigurationException("Не удалось прочитать поле: " + describe(field));
        }
    }

    private static ValidationConfigurationException unsupported(Field field, String annotation) {
        return new ValidationConfigurationException("@" + annotation + " нельзя применять к полю " + describe(field) + " типа " + field.getType().getName());
    }

    private static String describe(Field field) {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }
}