package validation;
import org.example.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidatorTest {
    static class User {
        @NotNull
        @Size(min = 2, max = 50)
        private String name;

        @NotNull
        @Email
        private String email;

        @Range(min = 0, max = 150)
        private Integer age;

        User(String name, String email, Integer age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }
    }

    static class Parent {
        @NotNull
        private String parentField;
    }

    static class Child extends Parent {
        @NotNull
        private String childField;
        @NotNull
        private static String ignored = null;
    }

    static class BadBounds {
        @Size(min = 5, max = 2)
        private String value = "abc";
    }

    static class WrongType {
        @Range(min = 0, max = 1)
        private String value = "1";
    }

    private static Set<String> errorKeys(Object object) {
        Set<String> keys = new HashSet<>();
        for (ValidationError error : Validator.validate(object).getErrors()) {
            keys.add(error.fieldName() + ":" + error.annotationName());
        }
        return keys;
    }

    @Test
    void validObject() {
        ValidationResult result = Validator.validate(new User("Anna", "anna@mail.ru", 20));

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void eachAnnotationDetectsViolation() {
        assertEquals(Set.of("name:NotNull"), errorKeys(new User(null, "a@b.ru", 20)));
        assertEquals(Set.of("name:Size"), errorKeys(new User("A", "a@b.ru", 20)));
        assertEquals(Set.of("age:Range"), errorKeys(new User("Anna", "a@b.ru", 200)));
        assertEquals(Set.of("email:Email"), errorKeys(new User("Anna", "wrong", 20)));
    }

    @Test
    void collectsAllErrors() {
        assertEquals(Set.of("name:Size", "email:Email", "age:Range"),
                errorKeys(new User("A", "wrong", 200)));
    }

    @ParameterizedTest
    @CsvSource({"2, true", "50, true", "1, false", "51, false"})
    void sizeBoundaries(int length, boolean valid) {
        User user = new User("a".repeat(length), "a@b.ru", 20);

        assertEquals(valid, Validator.validate(user).isValid());
    }

    @ParameterizedTest
    @CsvSource({"0, true", "150, true", "-1, false", "151, false"})
    void rangeBoundaries(int age, boolean valid) {
        User user = new User("Anna", "a@b.ru", age);

        assertEquals(valid, Validator.validate(user).isValid());
    }

    @Test
    void nullObjectThrowsNpe() {
        assertThrows(NullPointerException.class, () -> Validator.validate(null));
    }

    @Test
    void configurationErrorsThrowException() {
        assertThrows(ValidationConfigurationException.class,
                () -> Validator.validate(new BadBounds()));
        assertThrows(ValidationConfigurationException.class,
                () -> Validator.validate(new WrongType()));
    }

    @Test
    void privateInheritedAndStaticFields() {
        assertEquals(Set.of("parentField:NotNull", "childField:NotNull"),
                errorKeys(new Child()));
    }

    @Test
    void errorsListIsImmutable() {
        List<ValidationError> errors = Validator.validate(new User("A", "a@b.ru", 20)).getErrors();
        ValidationError extra = new ValidationError("x", "NotNull", "msg");

        assertThrows(UnsupportedOperationException.class, () -> errors.add(extra));
    }
}