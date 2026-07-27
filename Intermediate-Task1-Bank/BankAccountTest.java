import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;

    // @BeforeEach runs before EVERY test method, giving each test a fresh,
    // independent BankAccount so that tests never rely on each other's state.
    @BeforeEach
    void setUp() {
        account = new BankAccount("Test User", 1000);
    }

    // ---------------- DEPOSIT TESTS ----------------
    @Test
    void testDepositIncreasesBalance() {
        account.deposit(500);
        assertEquals(1500, account.getBalance(), "Balance should increase by the deposited amount");
    }

    @Test
    void testDepositWithNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100),
                "Depositing a negative amount should throw IllegalArgumentException");
    }

    @Test
    void testDepositWithZeroAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0),
                "Depositing zero should not be allowed");
    }

    // ---------------- WITHDRAWAL TESTS ----------------
    @Test
    void testWithdrawDecreasesBalance() {
        account.withdraw(300);
        assertEquals(700, account.getBalance(), "Balance should decrease by the withdrawn amount");
    }

    @Test
    void testWithdrawMoreThanBalanceThrowsException() {
        // Negative Testing: withdrawing more than the balance must throw InsufficientFundsException
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(5000),
                "Withdrawing more than the balance should throw InsufficientFundsException");
    }

    @Test
    void testWithdrawNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50),
                "Withdrawing a negative amount should throw IllegalArgumentException");
    }

    @Test
    void testWithdrawExactBalanceLeavesZero() {
        account.withdraw(1000);
        assertEquals(0, account.getBalance(), "Withdrawing the exact balance should leave zero");
    }

    // ---------------- BALANCE INQUIRY TESTS ----------------
    @Test
    void testInitialBalanceIsSetCorrectly() {
        assertEquals(1000, account.getBalance(), "Initial balance should match the constructor argument");
    }

    // ---------------- TRANSACTION HISTORY TESTS (Tier 1) ----------------
    @Test
    void testTransactionHistoryEmptyInitially() {
        assertTrue(account.getTransactionHistory().isEmpty(), "A new account should have no transactions yet");
    }

    @Test
    void testTransactionHistoryGrowsAfterDeposit() {
        account.deposit(200);
        assertEquals(1, account.getTransactionHistory().size(),
                "Transaction history should have exactly 1 entry after one deposit");
    }

    @Test
    void testTransactionHistoryGrowsAfterMultipleOperations() {
        account.deposit(200);
        account.withdraw(100);
        account.deposit(50);
        assertEquals(3, account.getTransactionHistory().size(),
                "Transaction history should record every deposit and withdrawal");
    }

    @Test
    void testTransactionHistoryRecordsCorrectType() {
        account.deposit(200);
        Transaction t = account.getTransactionHistory().get(0);
        assertEquals("DEPOSIT", t.getType(), "The recorded transaction type should be DEPOSIT");
    }

    @Test
    void testFailedWithdrawalDoesNotAddToHistory() {
        try {
            account.withdraw(5000); // this will fail (insufficient funds)
        } catch (InsufficientFundsException e) {
            // expected - ignore
        }
        assertTrue(account.getTransactionHistory().isEmpty(),
                "A failed withdrawal should NOT be recorded in the transaction history");
    }
}