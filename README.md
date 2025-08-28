# 🍽️ SAVE BITE (Phase I - Console)

## 📌 Project Overview
The **Save Bite** is a Java-based console application designed to help individuals and households minimize food waste.  
In this **Phase I**, the project focuses on **basic food inventory management** through the terminal.  
Users can:
- Add food items with details (name, quantity, expiry date).
- View all stored food items.
- Remove consumed/expired items.
- Track expiry dates to avoid unnecessary waste.

---

## 🎯 Problem Statement
Food waste is a global issue, leading to:
- Huge **economic losses** every year.
- Significant **environmental damage** (greenhouse gas emissions, wasted resources).  

By giving users a **simple digital tracker**, this application aims to:
- Reduce waste at the household level.
- Build better habits for sustainable living.
- Save money by consuming food before expiry.

---

## 🚀 Features (Phase I - Console)
✅ Add new food items with expiry date  
✅ View all items in the list  
✅ Remove items once consumed/expired  
✅ Simple console-based menu system  

---

## 🛠️ Tech Stack
- **Language**: Java (JDK 17 or later)  
- **Paradigm**: Object-Oriented Programming (OOP)  
- **Tools**: Any IDE (IntelliJ, Eclipse, VS Code)  

---

## 📂 Project Structure
```
FoodWasteReductionTracker/
│── Main.java
│── FoodItem.java
│── FoodTracker.java
│── README.md

```
---

## 📊 UML Class Diagram  

```
class FoodItem {
  - String name
  - int quantity
  - String expiryDate
  
  + FoodItem(String name, int quantity, String expiryDate)
  + getName(): String
  + setName(String name): void
  + getQuantity(): int
  + setQuantity(int quantity): void
  + getExpiryDate(): String
  + setExpiryDate(String expiryDate): void
  + toString(): String
}

class FoodTracker {
  - List<FoodItem> foodList
  
  + FoodTracker()
  + addFoodItem(FoodItem item): void
  + removeFoodItem(String name): boolean
  + viewAllItems(): void
  + findItemByName(String name): FoodItem
  + getTotalItems(): int
}

class Main {
  + main(String[] args): void
  - displayMenu(): void
  - handleUserChoice(int choice, FoodTracker tracker, Scanner sc): void
}
```
## Sample Output
```
==== Food Waste Reduction Tracker (Phase I) ====

==== Menu ====
1. Add Food Item
2. View All Items
3. Check Expired Items (expired or expiring today)
4. Remove Item (by name)
5. Update Item (by name)
6. Search Item (by name)
7. Show Near-expiry Items (next N days)
8. Clear All Data
9. Exit
Enter your choice:
```
