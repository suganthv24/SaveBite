import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FoodItem {
    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String name;
    private int quantity;
    private LocalDate expiryDate;

    public FoodItem(String name, int quantity, LocalDate expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    // For CSV loading
    public FoodItem(String name, int quantity, String expiryDateStr) {
        this(name, quantity, LocalDate.parse(expiryDateStr));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now()) || expiryDate.equals(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("%s | qty: %d | expiry: %s", name, quantity, expiryDate.format(DISPLAY_FMT));
    }

    // CSV representation: name,quantity,yyyy-MM-dd
    public String toCSV() {
        return String.format("%s,%d,%s", escapeCsv(name), quantity, expiryDate.toString());
    }

    public static FoodItem fromCSV(String csvLine) {
        // simple CSV split (assumes no commas in name; if needed, extend)
        String[] parts = csvLine.split(",", 3);
        if (parts.length < 3) return null;
        String n = unescapeCsv(parts[0]);
        int q = Integer.parseInt(parts[1]);
        LocalDate d = LocalDate.parse(parts[2]);
        return new FoodItem(n, q, d);
    }

    private static String escapeCsv(String s) {
        return s.replace(",", ""); // crude; remove commas to keep CSV simple
    }

    private static String unescapeCsv(String s) {
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodItem foodItem = (FoodItem) o;
        return Objects.equals(name.toLowerCase(), foodItem.name.toLowerCase())
                && Objects.equals(expiryDate, foodItem.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), expiryDate);
    }
}
