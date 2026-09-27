package ru.mipt.hw1;

public enum BankType {
    NEO("НеоКредит Банк", 0.01),
    AUM("Арум Финтех", 0.02),
    VTA("Вектор Альянс Банк", 0.00);

    public String name;
    public double comissionRate;

    BankType(String name, double comissionRate) {
        this.name = name;
        this.comissionRate = comissionRate;
    }

    public String getName() {
        return name;
    }

    public double getComissionRate() {
        return comissionRate;
    }
}
