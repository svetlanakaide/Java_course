package ru.mipt.hw1;

public class Account {
    public int cardNumber;
    public int pinCode;
    public double balance;
    public BankType bankType;

    public Account(int cardNumber, int pinCode, double balance, BankType bankType) {
        this.cardNumber = normalizeToDigits(cardNumber, 5);
        this.pinCode = normalizeToDigits(pinCode, 3);
        long rounded = Math.round(balance * 100.0);
        this.balance = rounded / 100.0;
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
            return max;
        }
        return abs;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public BankType getBankType() {
        return bankType;
    }

    @Override
    public String toString() {
        return "[" + bankType.getName() + "] Карта: [" + cardNumber + "], Баланс: [" + String.format("%.2f", balance) + "] руб.";
    }
}
