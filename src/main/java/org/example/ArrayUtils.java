package org.example;
import java.util.Objects;


public class ArrayUtils {
    public static <T> int findFirst(T[] array, T element) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], element)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        final String[] names = {"Alice", "Bob", "Charlie"};
        final int index = ArrayUtils.findFirst(names, "Bob");
        System.out.println(index);

        System.out.println(findFirst(names, null));
        System.out.println(findFirst(new String[]{"A", null, "B"}, null));
    }
}