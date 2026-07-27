import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {

    private final String accountHolder;
    private double balance;
    private final List<Transaction> transactionHistory = new ArrayList<>();

    public BankAccount(String accountHolder, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    // ---------------- DEPOSIT ----------------
    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        transactionHistory.add(new Transaction("DEPOSIT", amount, balance));
    }

    // ---------------- WITHDRAW ----------------
    // Tier 2 - Creative Upgrade: synchronized keyword prevents race conditions
    // when two threads try to withdraw money from the same account at the same time.
    public synchronized void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds: tried to withdraw " + amount + " but balance is only " + balance);
        }
        balance -= amount;
        transactionHistory.add(new Transaction("WITHDRAWAL", amount, balance));
    }

    // ---------------- BALANCE INQUIRY ----------------
    public synchronized double getBalance() {
        return balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    // ---------------- TRANSACTION HISTORY (Tier 1 - Grounded Upgrade) ----------------
    public synchronized List<Transaction> getTransactionHistory() {
        // Return a copy so external code can't modify the internal list directly
        return Collections.unmodifiableList(new ArrayList<>(transactionHistory));
    }

    // Prints a simple mini-statement of all transactions so far
    public synchronized void printMiniStatement() {
        System.out.println("\n---- Mini Statement for " + accountHolder + " ----");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println(t);
            }
        }
        System.out.println("Current Balance: " + balance);
        System.out.println("--------------------------------------------");
    }
}