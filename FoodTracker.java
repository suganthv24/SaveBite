import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FoodTracker {
    private final List<FoodItem> items;
    private final File storageFile;

    // If storageFile is null, persistence is disabled.
    public FoodTracker(File storageFile) {
        this.items = new ArrayList<>();
        this.storageFile = storageFile;
        if (storageFile != null && storageFile.exists()) {
            loadFromFile();
        }
    }

    public FoodTracker() {
        this(null);
    }

    public synchronized void addItem(FoodItem item) {
        // If same name & same expiry exists, increase quantity
        Optional<FoodItem> existing = items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(item.getName()) && i.getExpiryDate().equals(item.getExpiryDate()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        saveToFile();
    }

    public synchronized boolean removeItem(String name) {
        List<FoodItem> removed = items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        if (removed.isEmpty()) return false;
        items.removeAll(removed);
        saveToFile();
        return true;
    }

    public synchronized boolean removeSpecificItem(String name, LocalDate expiry) {
        Optional<FoodItem> found = items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && i.getExpiryDate().equals(expiry))
                .findFirst();
        if (found.isPresent()) {
            items.remove(found.get());
            saveToFile();
            return true;
        }
        return false;
    }

    public synchronized boolean updateItem(String oldName, int newQty, LocalDate newExpiry) {
        Optional<FoodItem> found = items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(oldName))
                .findFirst();
        if (!found.isPresent()) return false;
        FoodItem i = found.get();
        i.setQuantity(newQty);
        i.setExpiryDate(newExpiry);
        saveToFile();
        return true;
    }

    public synchronized List<FoodItem> viewAll() {
        // return sorted by expiry date ascending
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    public synchronized List<FoodItem> searchByName(String keyword) {
        String k = keyword.toLowerCase();
        return items.stream()
                .filter(i -> i.getName().toLowerCase().contains(k))
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    public synchronized List<FoodItem> getExpiredItems() {
        LocalDate today = LocalDate.now();
        return items.stream()
                .filter(i -> !i.getExpiryDate().isAfter(today)) // expired or expiring today
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    public synchronized List<FoodItem> getNearExpiryItems(int daysThreshold) {
        LocalDate limit = LocalDate.now().plusDays(daysThreshold);
        return items.stream()
                .filter(i -> !i.getExpiryDate().isAfter(limit))
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    /* Simple CSV persistence: each line = name,quantity,yyyy-MM-dd */
    public synchronized void saveToFile() {
        if (storageFile == null) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storageFile))) {
            for (FoodItem i : items) {
                bw.write(i.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    public synchronized void loadFromFile() {
        if (storageFile == null || !storageFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                FoodItem item = FoodItem.fromCSV(line.trim());
                if (item != null) items.add(item);
            }
        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }

    public synchronized void clearAll() {
        items.clear();
        saveToFile();
    }
}
