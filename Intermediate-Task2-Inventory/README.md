# Inventory Management System

This project is an Inventory Management System built for the ShadowFox Java Development Internship (Intermediate Level - Task: Inventory Management System with Basic GUI).

- **InventoryManager.java** - Console-based version
- **InventoryManagerGUI.java** - GUI version (Swing) with mint/teal theme

## Features

**Core CRUD Operations:** Add, View, Update, Delete products (barcode, name, quantity, price)

**Input Validation:**
- Product name cannot be empty
- Quantity and price cannot be negative
- Numeric fields validated before saving

**Auto-generated Barcode IDs:** Each product gets a unique barcode (starting from 1001) when added

## Tier 1 - Grounded Upgrade: Low Stock Alerts

Any product with quantity below 5 is automatically highlighted with a red background, red border, and a "LOW STOCK" tag. The header also shows a running count of how many items are currently low in stock, so the user can spot inventory issues at a glance without scanning the whole list.

## Tier 2 - Creative Upgrade: Barcode Search (Mockup)

A "Barcode ID" search field at the top lets the user type a barcode and press Enter to instantly locate a product. The matching card automatically scrolls into view and briefly flashes yellow to draw attention to it - a lightweight simulation of how a real barcode scanner would jump straight to a product in a POS/inventory system.

## How to Run

1. Make sure JDK is installed (`java -version` to check)
2. Compile and run the GUI version:

## Tech Used

Java, Swing (JFrame, JPanel, Timer for animations), OOP (Encapsulation via the Product class)
