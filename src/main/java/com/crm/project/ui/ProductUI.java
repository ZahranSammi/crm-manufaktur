package com.crm.project.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.crm.project.model.Product;
import com.crm.project.repository.ProductRepository;

/**
 * User Interface class for Product management.
 * Provides a Scanner-based menu for CRUD operations.
 */
public class ProductUI {

    private final ProductRepository productRepository;
    private final Scanner scanner;

    public ProductUI() {
        this.productRepository = new ProductRepository();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays and handles the Product Management menu.
     * Runs in a loop until user chooses to go back.
     */
    public void showMenu() {
        boolean running = true;

        while (running) {
            printMenuHeader();

            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 0:
                    running = false;
                    System.out.println("\n🔙 Returning to Main Menu...\n");
                    break;
                default:
                    System.out.println("\n⚠️  Invalid choice. Please enter 0-3.\n");
            }
        }
    }

    /**
     * Prints the styled menu header.
     */
    private void printMenuHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       📦 PRODUCT MANAGEMENT 📦       ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. 📋 Show All Products            ║");
        System.out.println("║   2. ➕ Add New Product              ║");
        System.out.println("║   3. 🗑️  Delete Product               ║");
        System.out.println("║   0. 🔙 Back to Main Menu            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("👉 Enter your choice: ");
    }

    /**
     * Gets menu choice from user with input validation.
     */
    private int getMenuChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    /**
     * Displays all products in a formatted table.
     */
    private void showAllProducts() {
        System.out.println("\n📋 ═══════════════════ ALL PRODUCTS ═══════════════════");

        List<Product> products = productRepository.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("   📭 No products found.");
        } else {
            System.out.println("┌───────┬───────────────────────────┬─────────────┬──────────┐");
            System.out.println("│  ID   │           Name            │    Price    │  Stock   │");
            System.out.println("├───────┼───────────────────────────┼─────────────┼──────────┤");
            for (Product product : products) {
                System.out.println(product);
            }
            System.out.println("└───────┴───────────────────────────┴─────────────┴──────────┘");
            System.out.println("   📊 Total: " + products.size() + " product(s)");
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    /**
     * Prompts user for product details and adds a new product.
     */
    private void addNewProduct() {
        System.out.println("\n➕ ═══════════════════ ADD NEW PRODUCT ═══════════════════");

        scanner.nextLine(); // Clear buffer

        System.out.print("   📝 Product Name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("   ❌ Error: Product name cannot be empty.\n");
            return;
        }

        double price;
        try {
            System.out.print("   💰 Price: $");
            price = scanner.nextDouble();
            if (price < 0) {
                System.out.println("   ❌ Error: Price cannot be negative.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid price format.\n");
            return;
        }

        int stock;
        try {
            System.out.print("   📦 Stock Quantity: ");
            stock = scanner.nextInt();
            if (stock < 0) {
                System.out.println("   ❌ Error: Stock cannot be negative.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid stock format.\n");
            return;
        }

        Product newProduct = new Product(name, price, stock);
        boolean success = productRepository.addProduct(newProduct);

        if (success) {
            System.out.println("\n   ✅ Product '" + name + "' added successfully!");
        } else {
            System.out.println("\n   ❌ Failed to add product. Please try again.");
        }
        System.out.println("════════════════════════════════════════════════════════\n");
    }

    /**
     * Prompts user for product ID and deletes the product.
     */
    private void deleteProduct() {
        System.out.println("\n🗑️  ═══════════════════ DELETE PRODUCT ═══════════════════");

        // Show current products first
        List<Product> products = productRepository.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("   📭 No products available to delete.");
            System.out.println("════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Current Products:");
        for (Product product : products) {
            System.out.println("   • ID: " + product.getId() + " - " + product.getName());
        }

        int id;
        try {
            System.out.print("\n   🔢 Enter Product ID to delete (0 to cancel): ");
            id = scanner.nextInt();

            if (id == 0) {
                System.out.println("   🚫 Delete operation cancelled.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid ID format.\n");
            return;
        }

        // Confirmation
        System.out.print("   ⚠️  Are you sure you want to delete product ID " + id + "? (y/n): ");
        scanner.nextLine(); // Clear buffer
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("   🚫 Delete operation cancelled.\n");
            return;
        }

        boolean success = productRepository.deleteProduct(id);

        if (success) {
            System.out.println("\n   ✅ Product deleted successfully!");
        } else {
            System.out.println("\n   ❌ Failed to delete. Product ID " + id + " may not exist.");
        }
        System.out.println("════════════════════════════════════════════════════════\n");
    }
}
