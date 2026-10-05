package ru.mipt.hw1;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    public int cardNumber;
    public int pinCode;
    public BigDecimal balance;
    public BankType bankType;

    public Account(int cardNumber, int pinCode, BigDecimal balance, BankType bankType) {
        this.cardNumber = normalizeToDigits(cardNumber, 5);
        this.pinCode = normalizeToDigits(pinCode, 3);

        BigDecimal safeBalance = (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : balance;
        this.balance = safeBalance.setScale(2, RoundingMode.HALF_UP);

        this.bankType = (bankType == null) ? BankType.NEO : bankType;
    }

    private int normalizeToDigits(int value, int digitCount) {
        int min = (int) Math.pow(10, digitCount - 1);
        int max = (int) Math.pow(10, digitCount) - 1;
        int abs = Math.abs(value);

        if (abs < min) {
            return min;
        }
        if (abs > max) {
            return abs % (max + 1) < min ? abs % (max + 1) + min : abs % (max + 1);
        }
        return abs;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BankType getBankType() {
        return bankType;
    }

    @Override
    public String toString() {
        return "[" + bankType.getName() + "] Карта: [" + cardNumber + "], Баланс: [" + balance.toPlainString() + "] руб.";
    }
}
