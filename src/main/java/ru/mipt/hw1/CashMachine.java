package ru.mipt.hw1;

public class CashMachine implements DepositOperations, WithdrawalOperations {

    @Override
    public double deposit(double balance, double amount) {
        if (amount <= 0) {
            return balance;
        }
        double newBalance = balance + amount;
        long rounded = Math.round(newBalance * 100.0);
        return rounded / 100.0;
    }

    @Override
    public double withdraw(double balance, Double amount, BankType bankType) {
        double comission = applyComission(amount, bankType);
        double totalToWithdraw = amount + comission;

        if (totalToWithdraw > balance) {
            System.out.println("Недостаточно средств на счете.");
            return balance;
        }

        double newBalance = balance - totalToWithdraw;
        long rounded = Math.round(newBalance * 100.0);
        return newBalance / 100.0;
    }
}
