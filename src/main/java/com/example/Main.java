package com.example;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.crm.project.modules.crm.ui.CrmUI;
import com.crm.project.modules.manufacturing.ui.ManufacturingUI;
import com.crm.project.ui.ProductUI;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      🚀 CRM APPLICATION STARTED 🚀     ║");
        System.out.println("╚════════════════════════════════════════╝");

        while (running) {
            printMainMenu();

            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    new ProductUI().showMenu();
                    break;
                case 2:
                    new CrmUI().showMenu();
                    break;
                case 3:
                    new ManufacturingUI().showMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("\n⚠️  Invalid choice. Please enter 0-3.\n");
            }
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  👋 Thank you for using CRM Application! ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static void printMainMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           📌 MAIN MENU 📌              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║   1.  Product Management             ║");
        System.out.println("║   2.  CRM (Customer Management)      ║");
        System.out.println("║   3.  Manufacturing                  ║");
        System.out.println("║   0.  Exit                           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("👉 Enter your choice: ");
    }

    private static int getMenuChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}