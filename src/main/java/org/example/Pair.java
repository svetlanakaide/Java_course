package org.example;


import java.util.Objects;

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Pair<V, K> swap() {
        return new Pair<>(value, key);
    }

    @Override
    public String toString() {
        return "Pair{key=" + key + ", value=" + value + "}";
    }

    private static void check(String name, Object actual, Object expected) {
        if (!Objects.equals(actual, expected)) {
            throw new AssertionError(name + ": ожидалось " + expected);
        }
    }

    public static void main(String[] args) {
        final Pair<String, Integer> pair = new Pair<>("Age", 25);
        System.out.println(pair);
        check("pair.key", pair.getKey(), "Age");
        check("pair.value", pair.getValue(), 25);
        check("pair.toString", pair.toString(), "Pair{key=Age, value=25}");

        final Pair<Integer, String> swapped = pair.swap();
        System.out.println(swapped);
        check("swapped.key", swapped.getKey(), 25);
        check("swapped.value", swapped.getValue(), "Age");
        check("swapped.toString", swapped.toString(), "Pair{key=25, value=Age}");

        final Pair<Object, Object> nullPair = new Pair<>(null, null);
        System.out.println(nullPair);
        check("nullPair.key", nullPair.getKey(), null);
        check("nullPair.value", nullPair.getValue(), null);
        check("nullPair.toString", nullPair.toString(), "Pair{key=null, value=null}");

        final Pair<Object, Object> nullSwapped = nullPair.swap();
        System.out.println(nullSwapped);
        check("nullSwapped.key", nullSwapped.getKey(), null);
        check("nullSwapped.value", nullSwapped.getValue(), null);
        check("nullSwapped.toString", nullSwapped.toString(), "Pair{key=null, value=null}");
        }
}