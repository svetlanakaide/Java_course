package ru.mipt.hw1;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать, dear friend!");

        Account referenceAccount = new Account(12345, 999, new BigDecimal("10000.00"), BankType.AUM);

        System.out.print("Введите номер карты, пожалуйста: ");
        String cardInput = scanner.nextLine().trim();
        if (!isValidNumber(cardInput)) {
            System.out.println("Номер карты должен быть числом.");
            return;
        }
        int enteredCardNumber = Integer.parseInt(cardInput);

        System.out.print("Введите пин-код, пожалуйста: ");
        String pinInput = scanner.nextLine().trim();
        if (!isValidNumber(pinInput)) {
            System.out.println("Пин-код должен быть числом.");
            return;
        }
        int enteredPinCode = Integer.parseInt(pinInput);

        if (enteredCardNumber != referenceAccount.getCardNumber() || enteredPinCode != referenceAccount.getPinCode()) {
            System.out.println("Неверный номер карты или пин-код.");
            return;
        }

        System.out.println("Вы авторизованы!");
        System.out.println(referenceAccount.toString());

        CashMachine cashMachine = new CashMachine();

        System.out.print("Введите сумму для внесения: ");
        String depositInput = scanner.nextLine().trim().replace(',', '.');
        if (!isValidNumber(depositInput)) {
            System.out.println("Сумма должна быть числом.");
            return;
        }
        BigDecimal depositAmount = new BigDecimal(depositInput);

        BigDecimal balanceAfterDeposit = cashMachine.deposit(referenceAccount.getBalance(), depositAmount);
        referenceAccount.balance = balanceAfterDeposit;
        System.out.println("Баланс после внесения: " + referenceAccount.getBalance().toPlainString() + " руб.");

        System.out.print("Введите сумму для снятия: ");
        String withdrawInput = scanner.nextLine().trim().replace(',', '.');
        if (!isValidNumber(withdrawInput)) {
            System.out.println("Сумма должна быть числом.");
            return;
        }
        BigDecimal withdrawAmount = new BigDecimal(withdrawInput);

        BigDecimal balanceAfterWithdraw = cashMachine.withdraw(referenceAccount.getBalance(), withdrawAmount, referenceAccount.getBankType());
        referenceAccount.balance = balanceAfterWithdraw;
        System.out.println("Баланс после снятия: " + referenceAccount.getBalance().toPlainString() + " руб.");

        System.out.println("Итоговое состояние счета: " + referenceAccount.toString());
    }

    private static boolean isValidNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int dotCount = 0;
        int digitCount = 0;
        int startIndex = (str.charAt(0) == '-') ? 1 : 0;
        if (startIndex == str.length()) {
            return false;
        }
        for (int i = startIndex; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                dotCount++;
                if (dotCount > 1) {
                    return false;
                }
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                return false;
            }
        }
        return digitCount > 0;
    }
}