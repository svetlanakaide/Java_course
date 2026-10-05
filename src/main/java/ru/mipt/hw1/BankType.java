package ru.mipt.hw1;
import java.math.BigDecimal;

public enum BankType {
    NEO("НеоКредит Банк", new BigDecimal("0.01")),
    AUM("Арум Финтех", new BigDecimal("0.02")),
    VTA("Вектор Альянс Банк", BigDecimal.ZERO);

    public String name;
    public BigDecimal commissionRate;

    BankType(String name, BigDecimal commissionRate) {
        this.name = name;
        this.commissionRate = commissionRate;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }
}
