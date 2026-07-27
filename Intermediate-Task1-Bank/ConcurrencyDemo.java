// Tier 2 - Creative Upgrade: Concurrency Safety
// This class simulates two threads trying to withdraw money from the SAME account
// at the exact same time, to prove that the "synchronized" keyword on withdraw()
// prevents a race condition (where both threads might read the old balance before
// either one updates it, resulting in an incorrect final balance or overdrawn account).
public class ConcurrencyDemo {

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount("Drashtee", 1000);

        System.out.println("Starting balance: " + account.getBalance());
        System.out.println("Two threads will each try to withdraw 700 at the same time...\n");

        Runnable withdrawTask = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                account.withdraw(700);
                System.out.println(threadName + " successfully withdrew 700. New balance: " + account.getBalance());
            } catch (InsufficientFundsException e) {
                System.out.println(threadName + " failed to withdraw: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(withdrawTask, "Thread-A");
        Thread t2 = new Thread(withdrawTask, "Thread-B");

        // Start both threads at almost the same time
        t1.start();
        t2.start();

        // Wait for both threads to finish before printing the final result
        t1.join();
        t2.join();

        System.out.println("\nFinal balance: " + account.getBalance());
        System.out.println("(Only ONE withdrawal of 700 should have succeeded, since balance was 1000."
                + " If both succeeded, that would mean the account went negative - a race condition bug.)");

        account.printMiniStatement();
    }
}