import org.example.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentRegistryTest {

    private final Student s1 = new Student(1, "A", 5.0);
    private final Student s2 = new Student(2, "B", 7.5);
    private final Student s3 = new Student(3, "C", 10.0);
    private final Student s4 = new Student(4, "D", 0.0);

    private Map<Integer, Student> hashMap;
    private NavigableMap<Integer, Student> treeMap;

    @BeforeEach
    void setUp() {
        List<Student> all = List.of(s1, s2, s3, s4);
        hashMap = StudentRegistry.toHashMap(all);
        treeMap = StudentRegistry.toDescendingTreeMap(all);
    }

    @Test
    void studentRejectsInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> new Student(0, "A", 5));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, " ", 5));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "A", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "A", 10.1));
    }

    @Test
    void equalStudentsHaveEqualHashCodes() {
        Student copy = new Student(1, "A", 5.0);
        assertEquals(s1, copy);
        assertEquals(s1.hashCode(), copy.hashCode());
    }

    @Test
    void hashMapUsesIdAsKey() {
        assertEquals(4, hashMap.size());
        assertEquals(s2, hashMap.get(2));
    }

    @Test
    void treeMapIsDescending() {
        assertEquals(List.of(4, 3, 2, 1), new ArrayList<>(treeMap.keySet()));
    }

    @Test
    void rangeIncludesBothBounds() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 5.0, 10.0);
        assertEquals(Set.of(s1, s2, s3), new HashSet<>(result));
    }

    @Test
    void rangeWithNoMatchesIsEmpty() {
        assertTrue(StudentRegistry.findStudentsByGradeRange(hashMap, 8.0, 9.0).isEmpty());
    }

    @Test
    void rangeOnEmptyMapIsEmpty() {
        assertTrue(StudentRegistry.findStudentsByGradeRange(Map.of(), 0.0, 10.0).isEmpty());
    }

    @Test
    void rangeRejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> StudentRegistry.findStudentsByGradeRange(hashMap, 6, 5));
        assertThrows(IllegalArgumentException.class, () -> StudentRegistry.findStudentsByGradeRange(hashMap, -1, 5));
        assertThrows(IllegalArgumentException.class, () -> StudentRegistry.findStudentsByGradeRange(hashMap, 0, 11));
    }

    @Test
    void rangeReturnsNewMutableList() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 0.0, 10.0);
        result.clear();
        assertEquals(4, hashMap.size());
    }

    @Test
    void topNReturnsLargestIdsInDescendingOrder() {
        assertEquals(List.of(s4, s3), StudentRegistry.getTopNStudentsById(treeMap, 2));
    }

    @Test
    void topNZeroIsEmpty() {
        assertTrue(StudentRegistry.getTopNStudentsById(treeMap, 0).isEmpty());
    }

    @Test
    void topNLargerThanMapReturnsAll() {
        assertEquals(List.of(s4, s3, s2, s1), StudentRegistry.getTopNStudentsById(treeMap, 100));
    }

    @Test
    void topNOnEmptyMapIsEmpty() {
        NavigableMap<Integer, Student> empty = StudentRegistry.toDescendingTreeMap(List.of());
        assertTrue(StudentRegistry.getTopNStudentsById(empty, 3).isEmpty());
    }

    @Test
    void topNRejectsNegativeN() {
        assertThrows(IllegalArgumentException.class, () -> StudentRegistry.getTopNStudentsById(treeMap, -1));
    }

    @Test
    void topNReturnsMutableListAndKeepsMap() {
        List<Student> result = StudentRegistry.getTopNStudentsById(treeMap, 2);
        result.clear();
        assertEquals(4, treeMap.size());
    }
}