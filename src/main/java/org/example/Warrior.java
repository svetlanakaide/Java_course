package org.example;

public class Warrior extends Hero {

    private final int armor;

    public int getArmor() {
        return armor;
    }

    public Warrior(String name, int maxHealth, int baseAttack, int armor) {
        super(name, maxHealth, baseAttack);
        if (armor < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Броня должна быть >= " + MIN_STAT_VALUE);
        }
        this.armor = armor;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        int finalDamage = Math.max(1, damage - armor);
        super.takeDamage(finalDamage);
    }

    @Override
    public void attack(Hero target) {
        System.out.println(getName() + " наносит урон"  + target.getName() + " (базовая атака: " + getBaseAttack() + ").");
        target.takeDamage(getBaseAttack());
    }

    @Override
    public ActionType makeTurn(Hero target) {
        if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        }
        if (needsRest()) {
            rest();
            return ActionType.REST;
        }
        attack(target);
        return ActionType.BASE_ATTACK;
    }

    @Override
    public boolean canCast() {
        return getHealth() * 2 < getMaxHealth();
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (!canCast()) {
            System.out.println("Воин упал");
            return;
        }
        int damage = getBaseAttack() + armor * 2;
        System.out.println(getName() + "наносит " + damage + " урона");
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest() {
        return getHealth() * 100 < getMaxHealth() * 15;
    }

    @Override
    public void rest() {
        int amount = armor * 2;
        heal(amount);
        System.out.println(getName() + "восстанавливает " + amount + " здоровья. Текущее здоровье: " + getHealth());
    }

    @Override
    public String getStatus() {
        return super.getStatus() + ", броня " + armor;
    }

}
