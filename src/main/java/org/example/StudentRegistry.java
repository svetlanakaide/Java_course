package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class StudentRegistry {

    private StudentRegistry() {
    }

    public static Map<Integer, Student> toHashMap(List<Student> students) {
        Map<Integer, Student> map = new HashMap<>();
        for (Student s : students) {
            map.put(s.id(), s);
        }
        return map;
    }

    public static NavigableMap<Integer, Student> toDescendingTreeMap(List<Student> students) {
        NavigableMap<Integer, Student> map = new TreeMap<>(Comparator.reverseOrder());
        for (Student s : students) {
            map.put(s.id(), s);
        }
        return map;
    }

    public static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students, double minGrade, double maxGrade) {
        if (!(minGrade >= 0.0 && maxGrade <= 10.0 && minGrade <= maxGrade)) {
            throw new IllegalArgumentException("Неверный диапазон: [" + minGrade + ", " + maxGrade + "]");
        }
        List<Student> result = new ArrayList<>();
        for (Student s : students.values()) {
            if (s.grade() >= minGrade && s.grade() <= maxGrade) {
                result.add(s);
            }
        }
        return result;
    }

    public static List<Student> getTopNStudentsById(NavigableMap<Integer, Student> students, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n должно быть >= 0: " + n);
        }
        Comparator<? super Integer> comparator = students.comparator();
        if (comparator == null || comparator.compare(1, 2) <= 0) {
            throw new IllegalArgumentException("Ключи карты должны быть отсортированы по убыванию");
        }
        List<Student> result = new ArrayList<>();
        for (Student s : students.values()) {
            if (result.size() == n) {
                break;
            }
            result.add(s);
        }
        return result;
    }
}