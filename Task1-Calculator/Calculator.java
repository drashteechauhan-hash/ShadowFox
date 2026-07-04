import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice;

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.next(); // clear bad input
                continue;
            }

            switch (choice) {
                case 1: add(sc); break;
                case 2: subtract(sc); break;
                case 3: multiply(sc); break;
                case 4: divide(sc); break;
                case 5: squareRoot(sc); break;
                case 6: power(sc); break;
                case 7: tempConversion(sc); break;
                case 8: currencyConversion(sc); break;
                case 9:
                    running = false;
                    System.out.println("Exiting... Bye!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== ENHANCED CALCULATOR =====");
        System.out.println("1. Add");
        System.out.println("2. Subtract");
        System.out.println("3. Multiply");
        System.out.println("4. Divide");
        System.out.println("5. Square Root");
        System.out.println("6. Power (Exponent)");
        System.out.println("7. Temperature Conversion");
        System.out.println("8. Currency Conversion (USD to INR)");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---------- Helper: safe number input ----------
    static BigDecimal readNumber(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.next();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Try again.");
            }
        }
    }

    // ---------- Basic Arithmetic ----------
    static void add(Scanner sc) {
        BigDecimal a = readNumber(sc, "Enter first number: ");
        BigDecimal b = readNumber(sc, "Enter second number: ");
        System.out.println("Result: " + a.add(b));
    }

    static void subtract(Scanner sc) {
        BigDecimal a = readNumber(sc, "Enter first number: ");
        BigDecimal b = readNumber(sc, "Enter second number: ");
        System.out.println("Result: " + a.subtract(b));
    }

    static void multiply(Scanner sc) {
        BigDecimal a = readNumber(sc, "Enter first number: ");
        BigDecimal b = readNumber(sc, "Enter second number: ");
        System.out.println("Result: " + a.multiply(b));
    }

    static void divide(Scanner sc) {
        BigDecimal a = readNumber(sc, "Enter first number: ");
        BigDecimal b = readNumber(sc, "Enter second number: ");
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Error: Cannot divide by zero!");
            return;
        }
        BigDecimal result = a.divide(b, 10, RoundingMode.HALF_UP);
        System.out.println("Result: " + result);
    }

    // ---------- Scientific ----------
    static void squareRoot(Scanner sc) {
        BigDecimal a = readNumber(sc, "Enter number: ");
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Error: Cannot take square root of negative number!");
            return;
        }
        double result = Math.sqrt(a.doubleValue());
        System.out.println("Result: " + result);
    }

    static void power(Scanner sc) {
        BigDecimal base = readNumber(sc, "Enter base: ");
        BigDecimal exponent = readNumber(sc, "Enter exponent: ");
        double result = Math.pow(base.doubleValue(), exponent.doubleValue());
        System.out.println("Result: " + result);
    }

    // ---------- Unit Conversion ----------
    static void tempConversion(Scanner sc) {
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.print("Choose: ");
        int opt;
        try {
            opt = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
            sc.next();
            return;
        }

        BigDecimal temp = readNumber(sc, "Enter temperature: ");

        if (opt == 1) {
            BigDecimal result = temp.multiply(BigDecimal.valueOf(9))
                    .divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP)
                    .add(BigDecimal.valueOf(32));
            System.out.println("Result: " + result + " °F");
        } else if (opt == 2) {
            BigDecimal result = temp.subtract(BigDecimal.valueOf(32))
                    .multiply(BigDecimal.valueOf(5))
                    .divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP);
            System.out.println("Result: " + result + " °C");
        } else {
            System.out.println("Invalid choice!");
        }
    }

    static void currencyConversion(Scanner sc) {
        BigDecimal usd = readNumber(sc, "Enter amount in USD: ");
        BigDecimal rate = BigDecimal.valueOf(83.50); // approx conversion rate
        BigDecimal result = usd.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        System.out.println("Result: ₹" + result);
    }
}