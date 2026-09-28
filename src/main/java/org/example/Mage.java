package org.example;

public class Mage extends Hero {

    private static final int SHOT_COST = 10;
    private static final int STAFF_MANA_GAIN = 5;
    private static final int SKILL_COST = 25;

    private final int maxMana;
    private int mana;

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana() {
        return mana;
    }

    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {
        super(name, maxHealth, baseAttack);
        if (maxMana < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Максимальная мана не может быть меньше " + MIN_STAT_VALUE);
        }
        this.maxMana = maxMana;
        this.mana = maxMana;
    }

    @Override
    public void attack(Hero target) {
        if (mana >= SHOT_COST) {
            int damage = getBaseAttack() * 2;
            mana -= SHOT_COST;
            System.out.println(getName() + " наносит " + damage + " урона! Осталось маны: " + mana);
            target.takeDamage(damage);
        } else {
            int damage = getBaseAttack() / 2;
            mana = Math.min(maxMana, mana + STAFF_MANA_GAIN);
            System.out.println(getName() + " избивает посохом " + target.getName() + " и наносит " + damage + " урона, восстанавливая " + STAFF_MANA_GAIN + " маны. Текущий статус маны: " + mana);
            target.takeDamage(damage);
        }
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
        return mana >= SKILL_COST;
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (!canCast()) {
            System.out.println(getName() + ": не хватает маны (нужно " + SKILL_COST + ", есть " + mana + ")");
            return;
        }
        int damage = getBaseAttack() * 3;
        mana -= SKILL_COST;
        System.out.println(getName() + " обрушивает глыбу на " + target.getName() + " и наносит " + damage + " урона. Осталось маны: " + mana);
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest() {
        return mana == 0 || getHealth() * 100 < getMaxHealth() * 30;
    }

    @Override
    public void rest() {
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println(getName() + "тратим ход на восстановление: мана полностью восстановлена (" + mana + "), здоровье +" + getBaseAttack() + ". Текущее здоровье: " + getHealth());
    }

    @Override
    public String getStatus() {
        return super.getStatus() + ", мана " + mana + "/" + maxMana;
    }
}
