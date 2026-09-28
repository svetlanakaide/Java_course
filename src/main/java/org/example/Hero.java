package org.example;

public abstract class Hero implements Castable, Restable {

    public static final int MIN_STAT_VALUE = 1;

    private final String name;
    private final int maxHealth;
    private final int baseAttack;
    private int health;

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getHealth() {
        return health;
    }

    public Hero(String name, int maxHealth, int baseAttack) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя персонажа не может быть пустым");
        }
        if (maxHealth < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Максимальное здоровье не может быть меньше " + MIN_STAT_VALUE);
        }
        if (baseAttack < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Базовая атака не может быть меньше " + MIN_STAT_VALUE);
        }

        this.name = name;
        this.maxHealth = maxHealth;
        this.baseAttack = baseAttack;
        this.health = maxHealth;
    }

    abstract void attack(Hero target);

    public abstract ActionType makeTurn(Hero target);

    public void takeDamage(int damage) {
        if (damage <= 0 || health == 0) {
            return;
        }
        health = Math.max(0, health - damage);
        if (health == 0) {
            System.out.println(name + " пал славной смертью!");
        }
    }

    void heal(int amount) {
        if (amount <= 0) {
            return;
        }
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String getStatus() {
        return name + " — здоровье " + health + "/" + maxHealth;
    }

}
