// Custom checked-style exception thrown when a withdrawal exceeds the available balance
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}