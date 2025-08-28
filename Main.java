import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter INPUT_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("==== Food Waste Reduction Tracker (Phase I) ====");
        // Create tracker with file persistence (data.csv) in project root
        File storage = new File("data.csv");
        FoodTracker tracker = new FoodTracker(storage);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    addItemFlow(sc, tracker);
                    break;
                case "2":
                    viewAllFlow(tracker);
                    break;
                case "3":
                    checkExpiredFlow(tracker);
                    break;
                case "4":
                    removeItemFlow(sc, tracker);
                    break;
                case "5":
                    updateItemFlow(sc, tracker);
                    break;
                case "6":
                    searchFlow(sc, tracker);
                    break;
                case "7":
                    nearExpiryFlow(sc, tracker);
                    break;
                case "8":
                    clearDataFlow(sc, tracker);
                    break;
                case "9":
                    System.out.println("Exiting. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from the menu.");
            }
        }

        sc.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==== Menu ====");
        System.out.println("1. Add Food Item");
        System.out.println("2. View All Items");
        System.out.println("3. Check Expired Items (expired or expiring today)");
        System.out.println("4. Remove Item (by name)");
        System.out.println("5. Update Item (by name)");
        System.out.println("6. Search Item (by name)");
        System.out.println("7. Show Near-expiry Items (next N days)");
        System.out.println("8. Clear All Data");
        System.out.println("9. Exit");
    }

    private static void addItemFlow(Scanner sc, FoodTracker tracker) {
        try {
            System.out.print("Enter food name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            System.out.print("Enter quantity (integer): ");
            String qStr = sc.nextLine().trim();
            int qty = Integer.parseInt(qStr);
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }

            System.out.print("Enter expiry date (DD/MM/YYYY): ");
            String dateStr = sc.nextLine().trim();
            LocalDate expiry;
            try {
                expiry = LocalDate.parse(dateStr, INPUT_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use DD/MM/YYYY.");
                return;
            }

            FoodItem item = new FoodItem(name, qty, expiry);
            tracker.addItem(item);
            System.out.println("✅ Food item added successfully!");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number. Please enter integer quantity.");
        } catch (Exception e) {
            System.out.println("Failed to add item: " + e.getMessage());
        }
    }

    private static void viewAllFlow(FoodTracker tracker) {
        List<FoodItem> all = tracker.viewAll();
        if (all.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("---- Inventory ----");
        int idx = 1;
        for (FoodItem i : all) {
            System.out.printf("%d. %s\n", idx++, i.toString());
        }
    }

    private static void checkExpiredFlow(FoodTracker tracker) {
        List<FoodItem> expired = tracker.getExpiredItems();
        if (expired.isEmpty()) {
            System.out.println("No expired or expiring items today.");
            return;
        }
        System.out.println("---- Expired / Expiring Today ----");
        for (FoodItem i : expired) {
            System.out.println(i.toString() + (i.isExpired() ? " [EXPIRED]" : ""));
        }
    }

    private static void removeItemFlow(Scanner sc, FoodTracker tracker) {
        System.out.print("Enter name to remove (exact name): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        boolean removed = tracker.removeItem(name);
        if (removed) {
            System.out.println("Items with name '" + name + "' removed.");
        } else {
            System.out.println("No item found with that name.");
        }
    }

    private static void updateItemFlow(Scanner sc, FoodTracker tracker) {
        System.out.print("Enter the name of the item to update: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        System.out.print("Enter new quantity (integer): ");
        String qStr = sc.nextLine().trim();
        int qty;
        try {
            qty = Integer.parseInt(qStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }
        System.out.print("Enter new expiry date (DD/MM/YYYY): ");
        String dateStr = sc.nextLine().trim();
        LocalDate expiry;
        try {
            expiry = LocalDate.parse(dateStr, INPUT_FMT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        boolean ok = tracker.updateItem(name, qty, expiry);
        if (ok) {
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("No item found with that name to update.");
        }
    }

    private static void searchFlow(Scanner sc, FoodTracker tracker) {
        System.out.print("Enter search keyword: ");
        String kw = sc.nextLine().trim();
        if (kw.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }
        List<FoodItem> found = tracker.searchByName(kw);
        if (found.isEmpty()) {
            System.out.println("No items match the keyword.");
            return;
        }
        System.out.println("---- Search Results ----");
        for (FoodItem i : found) {
            System.out.println(i.toString());
        }
    }

    private static void nearExpiryFlow(Scanner sc, FoodTracker tracker) {
        System.out.print("Show items expiring in how many days? (e.g., 3): ");
        String dStr = sc.nextLine().trim();
        int days;
        try {
            days = Integer.parseInt(dStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }
        List<FoodItem> near = tracker.getNearExpiryItems(days);
        if (near.isEmpty()) {
            System.out.println("No items expiring within " + days + " days.");
            return;
        }
        System.out.println("---- Near-expiry Items ----");
        for (FoodItem i : near) {
            System.out.println(i.toString());
        }
    }

    private static void clearDataFlow(Scanner sc, FoodTracker tracker) {
        System.out.print("Are you sure you want to clear all data? Type 'yes' to confirm: ");
        String conf = sc.nextLine().trim();
        if ("yes".equalsIgnoreCase(conf)) {
            tracker.clearAll();
            System.out.println("All data cleared.");
        } else {
            System.out.println("Cancelled.");
        }
    }
}
