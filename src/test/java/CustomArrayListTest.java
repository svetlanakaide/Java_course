import org.example.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CustomArrayListTest {

    private CustomArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomArrayList<>();
    }

    private void fillAbc() {
        list.add("a");
        list.add("b");
        list.add("c");
    }

    @Test
    void newListIsEmpty() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void addKeepsOrder() {
        list.add("a");
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));

        list.add("b");
        list.add("c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void growthKeepsData() {
        CustomArrayList<Integer> numbers = new CustomArrayList<>();
        for (int i = 0; i < 25; i++) {
            numbers.add(i);
        }
        assertEquals(25, numbers.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, numbers.get(i));
        }
    }

    @Test
    void getFirstMiddleLast() {
        fillAbc();
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    void getRejectsBadIndex(int index) {
        fillAbc();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    void removeRejectsBadIndex(int index) {
        fillAbc();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
    }

    @Test
    void removeFirst() {
        fillAbc();
        assertEquals("a", list.remove(0));
        assertEquals(2, list.size());
        assertEquals("b", list.get(0));
        assertEquals("c", list.get(1));
    }

    @Test
    void removeMiddle() {
        fillAbc();
        assertEquals("b", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(1));
    }

    @Test
    void removeLast() {
        fillAbc();
        assertEquals("c", list.remove(2));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }

    @Test
    void addNullThrows() {
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    void iteratorGoesInOrder() {
        fillAbc();
        Iterator<String> it = list.iterator();
        assertEquals("a", it.next());
        assertEquals("b", it.next());
        assertEquals("c", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void finishedIteratorThrows() {
        list.add("a");
        Iterator<String> it = list.iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }
}