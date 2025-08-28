🍴 SaveBite (Phase I - Console)
📌 Project Overview

SaveBite is a Java-based console application that helps individuals and households reduce food waste. In this phase, the focus is on building a simple command-line program where users can:

Add food items with details like name, quantity, and expiry date.

View the list of stored items.

Track and check items nearing expiry.

Delete or update food items as needed.

This phase lays the foundation for future enhancements with database integration (Phase II) and a JavaFX GUI (Phase III).

🎯 Features (Phase I - Console)

Add new food items.

View all stored food items.

Track expiry dates.

Remove consumed or expired items.

Update food item details.

🏗️ Project Structure
FoodWasteTracker/
│── src/
│   ├── Main.java
│   ├── FoodItem.java
│   ├── FoodTracker.java
│── README.md

📊 Class UML Diagram
classDiagram
    class FoodItem {
        - String name
        - int quantity
        - LocalDate expiryDate
        + FoodItem(name, quantity, expiryDate)
        + getName()
        + getQuantity()
        + getExpiryDate()
        + setQuantity(quantity)
        + toString()
    }

    class FoodTracker {
        - List~FoodItem~ foodList
        + addFoodItem(FoodItem)
        + removeFoodItem(String name)
        + viewAllItems()
        + checkExpiryItems()
        + updateFoodItem(String name, int quantity, LocalDate expiryDate)
    }

    class Main {
        + main(String[] args)
    }

    FoodTracker --> FoodItem
    Main --> FoodTracker

