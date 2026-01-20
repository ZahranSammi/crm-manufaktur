package com.crm.project.modules.manufacturing.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.crm.project.model.Material;
import com.crm.project.model.Product;
import com.crm.project.repository.MaterialRepository;
import com.crm.project.repository.ProductRepository;

/**
 * User Interface class for Manufacturing Module.
 * Handles material management and production operations.
 */
public class ManufacturingUI {

    private final MaterialRepository materialRepository;
    private final ProductRepository productRepository;
    private final Scanner scanner;

    public ManufacturingUI() {
        this.materialRepository = new MaterialRepository();
        this.productRepository = new ProductRepository();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            printMenuHeader();

            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    checkMaterialStock();
                    break;
                case 2:
                    addMaterialStock();
                    break;
                case 3:
                    produceProduct();
                    break;
                case 4:
                    addNewMaterial();
                    break;
                case 0:
                    running = false;
                    System.out.println("\n🔙 Returning to Main Menu...\n");
                    break;
                default:
                    System.out.println("\n⚠️  Invalid choice. Please enter 0-4.\n");
            }
        }
    }

    private void printMenuHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      🏭 MANUFACTURING MODULE 🏭      ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. 📦 Cek Stok Bahan               ║");
        System.out.println("║   2. 🛒 Belanja Bahan (Tambah Stok)  ║");
        System.out.println("║   3. ⚙️  PRODUKSI BARANG              ║");
        System.out.println("║   4. ➕ Tambah Bahan Baru            ║");
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

    private void checkMaterialStock() {
        System.out.println("\n📦 ═══════════════════ STOK BAHAN ═══════════════════");

        List<Material> materials = materialRepository.getAllMaterials();

        if (materials.isEmpty()) {
            System.out.println("   📭 No materials found. Add materials first!");
        } else {
            System.out.println("┌───────┬───────────────────────────┬────────────┐");
            System.out.println("│  ID   │        Nama Bahan         │    Stok    │");
            System.out.println("├───────┼───────────────────────────┼────────────┤");
            for (Material material : materials) {
                System.out.println(material);
            }
            System.out.println("└───────┴───────────────────────────┴────────────┘");
            System.out.println("   📊 Total: " + materials.size() + " jenis bahan");
        }
        System.out.println("══════════════════════════════════════════════════════\n");
    }

    private void addMaterialStock() {
        System.out.println("\n🛒 ═══════════════════ BELANJA BAHAN ═══════════════════");

        List<Material> materials = materialRepository.getAllMaterials();

        if (materials.isEmpty()) {
            System.out.println("   📭 No materials found. Add materials first using menu 4.");
            System.out.println("════════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Available Materials:");
        for (Material material : materials) {
            System.out.println("   • ID: " + material.getId() + " - " + material.getName() + " (Stok: "
                    + material.getStock() + ")");
        }

        int id;
        try {
            System.out.print("\n   🔢 Enter Material ID to add stock (0 to cancel): ");
            id = scanner.nextInt();

            if (id == 0) {
                System.out.println("   🚫 Operation cancelled.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid ID format.\n");
            return;
        }

        Material material = materialRepository.findById(id);
        if (material == null) {
            System.out.println("   ❌ Material ID " + id + " not found.\n");
            return;
        }

        int amount;
        try {
            System.out.print("   📦 Jumlah bahan yang dibeli: ");
            amount = scanner.nextInt();
            if (amount <= 0) {
                System.out.println("   ❌ Error: Amount must be positive.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid amount format.\n");
            return;
        }

        boolean success = materialRepository.addStock(id, amount);

        if (success) {
            System.out.println("\n   ✅ Successfully added " + amount + " units to '" + material.getName() + "'!");
            System.out.println("   📊 New stock: " + (material.getStock() + amount));
        } else {
            System.out.println("\n   ❌ Failed to add stock. Please try again.");
        }
        System.out.println("════════════════════════════════════════════════════════════\n");
    }

    private void addNewMaterial() {
        System.out.println("\n➕ ═══════════════════ TAMBAH BAHAN BARU ═══════════════════");

        scanner.nextLine();

        System.out.print("   📝 Nama Bahan: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("   ❌ Error: Material name cannot be empty.\n");
            return;
        }

        int stock;
        try {
            System.out.print("   📦 Stok Awal: ");
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

        Material newMaterial = new Material(name, stock);
        boolean success = materialRepository.addMaterial(newMaterial);

        if (success) {
            System.out.println("\n   ✅ Material '" + name + "' added successfully!");
        } else {
            System.out.println("\n   ❌ Failed to add material. Please try again.");
        }
        System.out.println("════════════════════════════════════════════════════════════════\n");
    }

    private void produceProduct() {
        System.out.println("\n⚙️  ═══════════════════ PRODUKSI BARANG ═══════════════════");

        // Show available materials
        List<Material> materials = materialRepository.getAllMaterials();
        if (materials.isEmpty()) {
            System.out.println("   📭 No materials available. Add materials first!");
            System.out.println("════════════════════════════════════════════════════════════\n");
            return;
        }

        // Show available products
        List<Product> products = productRepository.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("   📭 No products available. Add products first!");
            System.out.println("════════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   📦 Available Materials:");
        System.out.println("   ┌───────┬───────────────────────────┬────────────┐");
        System.out.println("   │  ID   │        Nama Bahan         │    Stok    │");
        System.out.println("   ├───────┼───────────────────────────┼────────────┤");
        for (Material m : materials) {
            System.out.printf("   │ %-5d │ %-25s │ %-10d │%n", m.getId(), m.getName(), m.getStock());
        }
        System.out.println("   └───────┴───────────────────────────┴────────────┘");

        System.out.println("\n   📋 Available Products:");
        System.out.println("   ┌───────┬───────────────────────────┬────────────┐");
        System.out.println("   │  ID   │        Nama Produk        │    Stok    │");
        System.out.println("   ├───────┼───────────────────────────┼────────────┤");
        for (Product p : products) {
            System.out.printf("   │ %-5d │ %-25s │ %-10d │%n", p.getId(), p.getName(), p.getStock());
        }
        System.out.println("   └───────┴───────────────────────────┴────────────┘");

        // Get input for production
        int materialId;
        try {
            System.out.print("\n   🔢 ID Bahan yang digunakan (0 to cancel): ");
            materialId = scanner.nextInt();
            if (materialId == 0) {
                System.out.println("   🚫 Production cancelled.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid ID format.\n");
            return;
        }

        Material selectedMaterial = materialRepository.findById(materialId);
        if (selectedMaterial == null) {
            System.out.println("   ❌ Material ID " + materialId + " not found.\n");
            return;
        }

        int amountUsed;
        try {
            System.out.print("   📦 Jumlah bahan yang dipakai: ");
            amountUsed = scanner.nextInt();
            if (amountUsed <= 0) {
                System.out.println("   ❌ Error: Amount must be positive.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid amount format.\n");
            return;
        }

        if (selectedMaterial.getStock() < amountUsed) {
            System.out.println("   ❌ Stok bahan tidak cukup! Stok tersedia: " + selectedMaterial.getStock());
            System.out.println("════════════════════════════════════════════════════════════\n");
            return;
        }

        int productId;
        try {
            System.out.print("   🎯 ID Produk yang akan diproduksi: ");
            productId = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid ID format.\n");
            return;
        }

        Product selectedProduct = productRepository.findById(productId);
        if (selectedProduct == null) {
            System.out.println("   ❌ Product ID " + productId + " not found.\n");
            return;
        }

        int productionQty;
        try {
            System.out.print("   �icing jumlah produk yang dihasilkan: ");
            productionQty = scanner.nextInt();
            if (productionQty <= 0) {
                System.out.println("   ❌ Error: Quantity must be positive.\n");
                return;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Invalid quantity format.\n");
            return;
        }

        // Execute production
        System.out.println("\n   ⏳ Processing production...");

        boolean materialReduced = materialRepository.reduceStock(materialId, amountUsed);
        if (!materialReduced) {
            System.out.println("   ❌ Failed to reduce material stock.\n");
            return;
        }

        boolean productAdded = productRepository.addStock(productId, productionQty);
        if (!productAdded) {
            // Rollback material if product stock update fails
            materialRepository.addStock(materialId, amountUsed);
            System.out.println("   ❌ Failed to add product stock. Material restored.\n");
            return;
        }

        System.out.println("\n   ═══════════════════════════════════════════════════════");
        System.out.println("   ✅ PRODUKSI BERHASIL!");
        System.out.println("   ───────────────────────────────────────────────────────");
        System.out.println("   📉 Bahan '" + selectedMaterial.getName() + "' dikurangi: " + amountUsed);
        System.out.println("   📈 Produk '" + selectedProduct.getName() + "' ditambah: " + productionQty);
        System.out.println("   ═══════════════════════════════════════════════════════");
        System.out.println("════════════════════════════════════════════════════════════\n");
    }
}
