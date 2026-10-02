package ru.mipt.hw1;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashMachine implements WithdrawalOperations, DepositOperations {

    @Override
    public BigDecimal deposit(BigDecimal balance, BigDecimal amount) {
        BigDecimal safeBalance = (balance == null) ? BigDecimal.ZERO : balance;

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return safeBalance.setScale(2, RoundingMode.HALF_UP);
        }
        return safeBalance.add(amount).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal withdraw(BigDecimal balance, BigDecimal amount, BankType bankType) {
        BigDecimal safeBalance = (balance == null) ? BigDecimal.ZERO : balance;

        if (amount == null || bankType == null) {
            System.out.println("Ошибка: сумма и тип банка не должны быть null.");
            return safeBalance.setScale(2, RoundingMode.HALF_UP);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Ошибка: сумма снятия должна быть положительной.");
            return safeBalance.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal commission = applyCommission(amount, bankType);
        BigDecimal totalToWithdraw = amount.add(commission);

        if (totalToWithdraw.compareTo(safeBalance) > 0) {
            System.out.println("Недостаточно средств на счете.");
            return safeBalance.setScale(2, RoundingMode.HALF_UP);
        }

        return safeBalance.subtract(totalToWithdraw).setScale(2, RoundingMode.HALF_UP);
    }
}
