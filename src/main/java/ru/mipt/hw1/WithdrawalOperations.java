package ru.mipt.hw1;

import org.w3c.dom.views.DocumentView;

public interface WithdrawalOperations {
    double withdraw(double balance, Double amount, BankType bankType);

    default double applyComission(Double amount, BankType bankType) {
        if (amount == null || bankType == null) {
            return 0.0;
        }
        double comission = amount + bankType.getComissionRate();
        long rounded = Math.round(comission * 100.0);
        return rounded / 100.0;
    }
}
