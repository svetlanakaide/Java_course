package org.example;

public enum ActionType {
    BASE_ATTACK("Обычная атака"),
    SPECIAL_SKILL("Особое умение"),
    REST("Восстановление");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
