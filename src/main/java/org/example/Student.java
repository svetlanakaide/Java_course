package org.example;

public record Student(int id, String name, double grade) {

    public Student {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным: " + id);
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("имя не должно быть пустым");
        }
        if (!(grade >= 0.0 && grade <= 10.0)) {
            throw new IllegalArgumentException("оценка должна быть от 0 до 10: " + grade);
        }
    }
}