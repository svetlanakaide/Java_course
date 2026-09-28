package org.example;

public class Arena {

    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        if (fighter1 == null || fighter2 == null) {
            throw new IllegalArgumentException("Оба бойца должны быть заданы");
        }
        if (fighter1 == fighter2) {
            throw new IllegalArgumentException("Боец не может сражаться сам с собой");
        }
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.roundCounter = 0;
    }

    public void startTournament() {
        System.out.println("=== Начало великого батла: " + fighter1.getName() + " против " + fighter2.getName() + " ===");

        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;
            System.out.println("\nРаунд " + roundCounter);

            Hero attacker;
            Hero defender;
            if (Math.random() < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            performTurn(attacker, defender);
            if (!defender.isAlive()) {
                break;
            }

            performTurn(defender, attacker);

            printStatus();
        }

        System.out.println("\nПоединок окончен за " + roundCounter + " раундов");
        printStatus();
        Hero winner = fighter1.isAlive() ? fighter1 : fighter2;
        System.out.println("Победитель: " + winner.getName());
    }

    private void performTurn(Hero actor, Hero target) {
        ActionType action = actor.makeTurn(target);
        System.out.println("Игрок " + actor.getName() + " совершил действие: " + action.getDescription());
    }

    private void printStatus() {
        System.out.println("Статус боя на данный момент: " + fighter1.getStatus() + " | " + fighter2.getStatus());
    }
}
