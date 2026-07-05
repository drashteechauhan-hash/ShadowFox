import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ContactManager {

    static ArrayList<Contact> contacts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // Email validation pattern
    static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

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
                case 1: addContact(); break;
                case 2: viewContacts(); break;
                case 3: searchContact(); break;
                case 4: updateContact(); break;
                case 5: deleteContact(); break;
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
        System.out.println("\n==== CONTACT MANAGEMENT SYSTEM ====");
        System.out.println("1. Add Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contact");
        System.out.println("4. Update Contact");
        System.out.println("5. Delete Contact");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---------------- ADD ----------------
    static void addContact() {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        String phone = readValidPhone();
        if (phone == null) return;

        // Duplicate check
        for (Contact c : contacts) {
            if (c.phone.equals(phone)) {
                System.out.println("A contact with this phone number already exists: " + c.name);
                return;
            }
        }

        String email = readValidEmail();
        if (email == null) return;

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added successfully!");
    }

    // ---------------- VIEW ----------------
    static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\n--- All Contacts (" + contacts.size() + ") ---");
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            System.out.println((i + 1) + ". " + c.name + " | " + c.phone + " | " + c.email);
        }
    }

    // ---------------- SEARCH ----------------
    static void searchContact() {
        System.out.print("Enter name to search: ");
        String query = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.toLowerCase().contains(query)) {
                System.out.println("Found: " + c.name + " | " + c.phone + " | " + c.email);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching contact found.");
        }
    }

    // ---------------- UPDATE ----------------
    static void updateContact() {
        System.out.print("Enter phone number of contact to update: ");
        String phone = sc.nextLine().trim();

        Contact target = null;
        for (Contact c : contacts) {
            if (c.phone.equals(phone)) {
                target = c;
                break;
            }
        }

        if (target == null) {
            System.out.println("Contact not found.");
            return;
        }

        System.out.println("Editing: " + target.name + " | " + target.phone + " | " + target.email);

        System.out.print("Enter new name (leave blank to keep '" + target.name + "'): ");
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) {
            target.name = newName;
        }

        System.out.print("Enter new email (leave blank to keep '" + target.email + "'): ");
        String newEmail = sc.nextLine().trim();
        if (!newEmail.isEmpty()) {
            if (EMAIL_PATTERN.matcher(newEmail).matches()) {
                target.email = newEmail;
            } else {
                System.out.println("Invalid email format. Email not updated.");
            }
        }

        System.out.println("Contact updated successfully!");
    }

    // ---------------- DELETE ----------------
    static void deleteContact() {
        System.out.print("Enter phone number of contact to delete: ");
        String phone = sc.nextLine().trim();

        Contact target = null;
        for (Contact c : contacts) {
            if (c.phone.equals(phone)) {
                target = c;
                break;
            }
        }

        if (target == null) {
            System.out.println("Contact not found.");
            return;
        }

        System.out.print("Are you sure you want to delete " + target.name + "? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            contacts.remove(target);
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ---------------- VALIDATION HELPERS ----------------
    static String readValidPhone() {
        System.out.print("Enter phone number (digits only, 10 digits): ");
        String phone = sc.nextLine().trim();

        if (!phone.matches("\\d{10}")) {
            System.out.println("Invalid phone number! Must be exactly 10 digits.");
            return null;
        }
        return phone;
    }

    static String readValidEmail() {
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Invalid email format!");
            return null;
        }
        return email;
    }
}

// Contact class (POJO - Plain Old Java Object) - demonstrates Encapsulation
class Contact {
    String name;
    String phone;
    String email;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}