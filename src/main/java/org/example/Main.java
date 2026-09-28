package org.example;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Создание бойцов");
        Hero fighter1 = createHero(1);
        Hero fighter2 = createHero(2);

        Arena arena = new Arena(fighter1, fighter2);
        arena.startTournament();

        scanner.close();
    }

    private static Hero createHero(int number) {
        System.out.println("\nБоец №" + number);

        int type = readIntInRange("Тип персонажа (1 — Воин, 2 — Маг): ", 1, 2);
        String name = readName("Имя: ");
        int maxHealth = readInt("Максимальное здоровье: ", Hero.MIN_STAT_VALUE);
        int baseAttack = readInt("Базовая атака: ", Hero.MIN_STAT_VALUE);

        if (type == 1) {
            int armor = readInt("Броня: ", Hero.MIN_STAT_VALUE);
            return new Warrior(name, maxHealth, baseAttack, armor);
        } else {
            int maxMana = readInt("Максимальная мана: ", Hero.MIN_STAT_VALUE);
            return new Mage(name, maxHealth, baseAttack, maxMana);
        }
    }

    private static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Имя не может быть пустым, попробуйте ещё раз.");
        }
    }

    private static int readInt(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min) {
                    return value;
                }
                System.out.println("Значение должно быть не меньше " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt, min);
            if (value <= max) {
                return value;
            }
            System.out.println("Значение должно быть от " + min + " до " + max + ".");
        }
    }
}