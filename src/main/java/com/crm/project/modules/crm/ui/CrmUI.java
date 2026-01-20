package com.crm.project.modules.crm.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.crm.project.model.Customer;
import com.crm.project.repository.CustomerRepository;

/**
 * User Interface class for CRM (Customer Management).
 * Provides a Scanner-based menu for CRUD operations.
 */
public class CrmUI {

    private final CustomerRepository customerRepository;
    private final Scanner scanner;

    public CrmUI() {
        this.customerRepository = new CustomerRepository();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            printMenuHeader();

            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    showAllCustomers();
                    break;
                case 2:
                    addNewCustomer();
                    break;
                case 3:
                    deleteCustomer();
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

    private void printMenuHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      👥 CUSTOMER MANAGEMENT 👥       ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. 📋 Show All Customers           ║");
        System.out.println("║   2. ➕ Add New Customer             ║");
        System.out.println("║   3. 🗑️  Delete Customer              ║");
        System.out.println("║   0. 🔙 Back to Main Menu            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("👉 Enter your choice: ");
    }

    private int getMenuChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void showAllCustomers() {
        System.out.println("\n📋 ═══════════════════ ALL CUSTOMERS ═══════════════════");

        List<Customer> customers = customerRepository.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("   📭 No customers found.");
        } else {
            System.out.println("┌───────┬───────────────────────────┬─────────────────┐");
            System.out.println("│  ID   │           Name            │      Phone      │");
            System.out.println("├───────┼───────────────────────────┼─────────────────┤");
            for (Customer customer : customers) {
                System.out.println(customer);
            }
            System.out.println("└───────┴───────────────────────────┴─────────────────┘");
            System.out.println("   📊 Total: " + customers.size() + " customer(s)");
        }
        System.out.println("═════════════════════════════════════════════════════════\n");
    }

    private void addNewCustomer() {
        System.out.println("\n➕ ═══════════════════ ADD NEW CUSTOMER ═══════════════════");

        scanner.nextLine();

        System.out.print("   📝 Customer Name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("   ❌ Error: Customer name cannot be empty.\n");
            return;
        }

        System.out.print("   📞 Phone Number: ");
        String phone = scanner.nextLine().trim();

        if (phone.isEmpty()) {
            System.out.println("   ❌ Error: Phone number cannot be empty.\n");
            return;
        }

        Customer newCustomer = new Customer(name, phone);
        boolean success = customerRepository.addCustomer(newCustomer);

        if (success) {
            System.out.println("\n   ✅ Customer '" + name + "' added successfully!");
        } else {
            System.out.println("\n   ❌ Failed to add customer. Please try again.");
        }
        System.out.println("════════════════════════════════════════════════════════════\n");
    }

    private void deleteCustomer() {
        System.out.println("\n🗑️  ═══════════════════ DELETE CUSTOMER ═══════════════════");

        List<Customer> customers = customerRepository.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("   📭 No customers available to delete.");
            System.out.println("════════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Current Customers:");
        for (Customer customer : customers) {
            System.out.println("   • ID: " + customer.getId() + " - " + customer.getName());
        }

        int id;
        try {
            System.out.print("\n   🔢 Enter Customer ID to delete (0 to cancel): ");
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

        System.out.print("   ⚠️  Are you sure you want to delete customer ID " + id + "? (y/n): ");
        scanner.nextLine();
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("   🚫 Delete operation cancelled.\n");
            return;
        }

        boolean success = customerRepository.deleteCustomer(id);

        if (success) {
            System.out.println("\n   ✅ Customer deleted successfully!");
        } else {
            System.out.println("\n   ❌ Failed to delete. Customer ID " + id + " may not exist.");
        }
        System.out.println("════════════════════════════════════════════════════════════\n");
    }
}
