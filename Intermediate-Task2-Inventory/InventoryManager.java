import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManager {

    static ArrayList<Product> products = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextBarcode = 1001; // simple auto-generated barcode counter

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: addProduct(); break;
                case 2: viewProducts(); break;
                case 3: updateProduct(); break;
                case 4: deleteProduct(); break;
                case 5: searchByBarcode(); break;
                case 0:
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
        System.out.println("\n==== INVENTORY MANAGEMENT SYSTEM ====");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Search by Barcode");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---------------- ADD ----------------
    static void addProduct() {
        System.out.print("Enter product name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Product name cannot be empty!");
            return;
        }

        int quantity = readValidInt("Enter quantity: ");
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return;
        }

        double price = readValidDouble("Enter price per unit (Rs): ");
        if (price < 0) {
            System.out.println("Price cannot be negative!");
            return;
        }

        String barcode = String.valueOf(nextBarcode++);
        products.add(new Product(barcode, name, quantity, price));
        System.out.println("Product added successfully! Barcode ID: " + barcode);
    }

    // ---------------- VIEW ----------------
    static void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }
        System.out.println("\n--- All Products (" + products.size() + ") ---");
        System.out.printf("%-10s %-20s %-10s %-10s %-10s%n", "Barcode", "Name", "Qty", "Price", "Total");
        for (Product p : products) {
            String lowStockTag = p.quantity < 5 ? "  [LOW STOCK]" : "";
            System.out.printf("%-10s %-20s %-10d Rs.%-8.2f Rs.%-8.2f%s%n",
                    p.barcode, p.name, p.quantity, p.price, p.getTotalValue(), lowStockTag);
        }
    }

    // ---------------- UPDATE ----------------
    static void updateProduct() {
        System.out.print("Enter barcode of product to update: ");
        String barcode = sc.nextLine().trim();

        Product target = findByBarcode(barcode);
        if (target == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Editing: " + target.name + " | Qty: " + target.quantity + " | Price: Rs." + target.price);

        System.out.print("Enter new name (leave blank to keep '" + target.name + "'): ");
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) {
            target.name = newName;
        }

        System.out.print("Enter new quantity (leave blank to keep " + target.quantity + "): ");
        String qtyInput = sc.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                int newQty = Integer.parseInt(qtyInput);
                if (newQty < 0) {
                    System.out.println("Quantity cannot be negative. Not updated.");
                } else {
                    target.quantity = newQty;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Not updated.");
            }
        }

        System.out.print("Enter new price (leave blank to keep Rs." + target.price + "): ");
        String priceInput = sc.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(priceInput);
                if (newPrice < 0) {
                    System.out.println("Price cannot be negative. Not updated.");
                } else {
                    target.price = newPrice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Not updated.");
            }
        }

        System.out.println("Product updated successfully!");
    }

    // ---------------- DELETE ----------------
    static void deleteProduct() {
        System.out.print("Enter barcode of product to delete: ");
        String barcode = sc.nextLine().trim();

        Product target = findByBarcode(barcode);
        if (target == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Are you sure you want to delete " + target.name + "? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            products.remove(target);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ---------------- SEARCH BY BARCODE (Tier 2 - Creative Upgrade) ----------------
    static void searchByBarcode() {
        System.out.print("Enter barcode ID: ");
        String barcode = sc.nextLine().trim();

        Product p = findByBarcode(barcode);
        if (p == null) {
            System.out.println("No product found with this barcode.");
            return;
        }

        String lowStockTag = p.quantity < 5 ? " [LOW STOCK]" : "";
        System.out.println("Found: " + p.name + " | Qty: " + p.quantity + " | Price: Rs." + p.price
                + " | Total Value: Rs." + p.getTotalValue() + lowStockTag);
    }

    // ---------------- HELPERS ----------------
    static Product findByBarcode(String barcode) {
        for (Product p : products) {
            if (p.barcode.equals(barcode)) {
                return p;
            }
        }
        return null;
    }

    static int readValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Try again.");
            }
        }
    }

    static double readValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Try again.");
            }
        }
    }
}

// Product class (POJO) - demonstrates Encapsulation
class Product {
    String barcode;
    String name;
    int quantity;
    double price;

    Product(String barcode, String name, int quantity, double price) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    double getTotalValue() {
        return quantity * price;
    }
}