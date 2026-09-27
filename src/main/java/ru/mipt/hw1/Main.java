package ru.mipt.hw1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать, dear friend!");

        Account referenceAccount = new Account(12345, 999, 10000.00, BankType.AUM);

        System.out.print("Введите номер карты, пожалуйста: ");
        String cardInput = scanner.nextLine().trim();
        if (!isValidNum(cardInput)) {
            System.out.println("Номер карты должен быть числом.");
            return;
        }
        int enteredCardNumber = Integer.parseInt(cardInput);

        System.out.print("Введите пин-код, пожалуйста: ");
        String pinInput = scanner.nextLine().trim();
        if (!isValidNum(pinInput)) {
            System.out.println("Пин-код должен быть числом.");
            return;
        }
        int enteredPinCode = Integer.parseInt(pinInput);

        if (enteredCardNumber != referenceAccount.getCardNumber() || enteredPinCode != referenceAccount.getPinCode()) {
            System.out.println("Неверный номер карты или пин-код.");
            return;
        }

        System.out.println("Вы авторизованы");
        System.out.println(referenceAccount.toString());

        CashMachine cashMachine = new CashMachine();

        System.out.print("Введите сумму для внесения: ");
        String depositInput = scanner.nextLine().trim().replace(',', '.');
        if (!isValidNum(depositInput)) {
            System.out.println("Сумма должна быть числом.");
            return;
        }
        double depositAmount = Double.parseDouble(depositInput);

        double balanceAfterDeposit = cashMachine.deposit(referenceAccount.getBalance(), depositAmount);
        referenceAccount.balance = balanceAfterDeposit;
        System.out.println("Баланс после внесения: " + String.format("%.2f", referenceAccount.getBalance()) + " руб.");

        System.out.print("Введите сумму для снятия: ");
        String withdrawInput = scanner.nextLine().trim().replace(',', '.');
        if (!isValidNum(withdrawInput)) {
            System.out.println("Сумма должна быть числом.");
            return;
        }
        double withdrawAmount = Double.parseDouble(withdrawInput);

        double balanceAfterWithdraw = cashMachine.withdraw(referenceAccount.getBalance(), withdrawAmount, referenceAccount.getBankType());
        referenceAccount.balance = balanceAfterWithdraw;
        System.out.println("Баланс после снятия: " + String.format("%.2f", referenceAccount.getBalance()) + " руб.");

        System.out.println("Итоговое состояние счета: " + referenceAccount.toString());
    }

    private static boolean isValidNum(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int startIndex = (str.charAt(0) == '-') ? 1 : 0;
        if (startIndex == str.length()) {
            return false;
        }
        return true;
    }
}