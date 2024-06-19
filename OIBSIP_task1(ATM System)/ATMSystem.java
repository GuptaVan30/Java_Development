package atmSystemm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ATM class
class ATM {
    private User user;

    public ATM(User user) {
        this.user = user;
    }

    public void withdraw(double amount) {
        if (amount > user.getAccount().getBalance()) {
            System.out.println("Insufficient funds!");
        } else {
            user.getAccount().setBalance(user.getAccount().getBalance() - amount);
            user.getAccount().addTransaction(new Transaction("Withdraw", amount));
            System.out.println("$" + amount + " withdrawn successfully.");
        }
    }

    public void deposit(double amount) {
        user.getAccount().setBalance(user.getAccount().getBalance() + amount);
        user.getAccount().addTransaction(new Transaction("Deposit", amount));
        System.out.println("$" + amount + " deposited successfully.");
    }

    public void transfer(double amount, User recipient) {
        if (amount > user.getAccount().getBalance()) {
            System.out.println("Insufficient funds!");
        } else {
            user.getAccount().setBalance(user.getAccount().getBalance() - amount);
            recipient.getAccount().setBalance(recipient.getAccount().getBalance() + amount);
            user.getAccount().addTransaction(new Transaction("Transfer to " + recipient.getUserId(), amount));
            recipient.getAccount().addTransaction(new Transaction("Received from " + user.getUserId(), amount));
            System.out.println("$" + amount + " transferred successfully to " + recipient.getUserId());
        }
    }
}

// User class
class User {
    private String userId;
    private String pin;
    private Account account;

    public User(String userId, String pin, Account account) {
        this.userId = userId;
        this.pin = pin;
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }
}

// Account class
class Account {
    private double balance;
    private List<Transaction> transactions;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

// Transaction class
class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

// ATMSystem class
public class ATMSystem {
    private Map<String, User> users;
    private Scanner scanner;

    public ATMSystem() {
        users = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User authenticate() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        User user = users.get(userId);
        if (user != null && user.getPin().equals(pin)) {
            return user;
        } else {
            System.out.println("Invalid user ID or PIN.");
            return null;
        }
    }

    public void run() {
        while (true) {
            User user = authenticate();
            if (user != null) {
                ATM atm = new ATM(user);
                while (true) {
                    System.out.println("\n1. Transaction History");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Deposit");
                    System.out.println("4. Transfer");
                    System.out.println("5. Quit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    if (choice == 5) {
                        break;
                    }

                    switch (choice) {
                        case 1:
                            System.out.println("Transaction History:");
                            for (Transaction t : user.getAccount().getTransactions()) {
                                System.out.println(t.getType() + ": $" + t.getAmount());
                            }
                            break;
                        case 2:
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            scanner.nextLine();  // Consume newline
                            atm.withdraw(withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            scanner.nextLine();  // Consume newline
                            atm.deposit(depositAmount);
                            break;
                        case 4:
                            System.out.print("Enter recipient user ID: ");
                            String recipientId = scanner.nextLine();
                            User recipient = users.get(recipientId);
                            if (recipient == null) {
                                System.out.println("Recipient not found.");
                                break;
                            }
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            scanner.nextLine();  // Consume newline
                            atm.transfer(transferAmount, recipient);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem();
        atmSystem.addUser(new User("user1", "1234", new Account(1000)));
        atmSystem.addUser(new User("user2", "5678", new Account(500)));
        atmSystem.run();
    }
}

