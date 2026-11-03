package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class CollectionPerformanceExperiment {

    private static final int N = 10_000;
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURED_RUNS = 5;

    private static final String[] NAMES = {
            "добавление в конец", "добавление в начало", "вставка в середину",
            "доступ по индексу", "удаление с начала", "удаление с конца"
    };

    private static long checksum;

    private CollectionPerformanceExperiment() {
    }

    public static void main(String[] args) {
        System.out.printf("%-22s %10s %16s %16s%n",
                "Операция", "Элементов", "ArrayList, мс", "LinkedList, мс");
        for (int op = 0; op < NAMES.length; op++) {
            double arrayTime = measure(op, false);
            double linkedTime = measure(op, true);
            System.out.printf("%-22s %10d %16.3f %16.3f%n", NAMES[op], N, arrayTime, linkedTime);
        }
        System.out.println("Контрольная сумма: " + checksum);
    }

    private static double measure(int op, boolean linked) {
        long[] times = new long[MEASURED_RUNS];
        for (int run = 0; run < WARMUP_RUNS + MEASURED_RUNS; run++) {
            List<Integer> list = linked ? new LinkedList<>() : new ArrayList<>();
            if (op >= 3) {
                fill(list);
            }
            long start = System.nanoTime();
            checksum += runOperation(op, list);
            long elapsed = System.nanoTime() - start;
            if (run >= WARMUP_RUNS) {
                times[run - WARMUP_RUNS] = elapsed;
            }
        }
        Arrays.sort(times);
        return times[MEASURED_RUNS / 2] / 1_000_000.0;
    }

    private static long runOperation(int op, List<Integer> list) {
        long sum = 0;
        switch (op) {
            case 0:
                for (int i = 0; i < N; i++) {
                    list.add(i);
                }
                return list.size();
            case 1:
                for (int i = 0; i < N; i++) {
                    list.add(0, i);
                }
                return list.size();
            case 2:
                for (int i = 0; i < N; i++) {
                    list.add(list.size() / 2, i);
                }
                return list.size();
            case 3:
                for (int i = 0; i < N; i++) {
                    sum += list.get(i);
                }
                return sum;
            case 4:
                while (!list.isEmpty()) {
                    sum += list.remove(0);
                }
                return sum;
            default:
                while (!list.isEmpty()) {
                    sum += list.remove(list.size() - 1);
                }
                return sum;
        }
    }

    private static void fill(List<Integer> list) {
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
    }
}