import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Represents a single transaction record - used for the mini-statement (Tier 1)
public class Transaction {
    private final String type;       // "DEPOSIT" or "WITHDRAWAL"
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    @Override
    public String toString() {
        return timestamp.format(FORMATTER) + "  |  " + type + "  |  Amount: " + amount
                + "  |  Balance After: " + balanceAfter;
    }
}