package com.crm.project;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.crm.project.features.crm.CrmUI;
import com.crm.project.features.pabrik.ManufacturingUI;
import com.crm.project.features.produk.ProductUI;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        tampilkanSambutan();

        boolean aplikasiBerjalan = true;

        while (aplikasiBerjalan) {
            tampilkanMenuUtama();
            int pilihanUser = bacaPilihanUser();

            switch (pilihanUser) {
                case 1 -> {
                    ProductUI uiProduk = new ProductUI();
                    uiProduk.tampilkanMenu();
                }
                case 2 -> {
                    CrmUI uiCustomer = new CrmUI();
                    uiCustomer.tampilkanMenu();
                }
                case 3 -> {
                    ManufacturingUI uiPabrik = new ManufacturingUI();
                    uiPabrik.tampilkanMenu();
                }
                case 0 -> {
                    aplikasiBerjalan = false;
                }
                default -> {
                    System.out.println("\n⚠️ Pilihan tidak valid. Silakan masukkan 0-3.\n");
                }
            }
        }

        tampilkanPerpisahan();
    }

    private static void tampilkanSambutan() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║       🏢 SELAMAT DATANG DI SISTEM MANAJEMEN TOKO 🏢          ║");
        System.out.println("║                                                              ║");
        System.out.println("║       Aplikasi untuk mengelola:                              ║");
        System.out.println("║       • Produk (barang dagangan)                             ║");
        System.out.println("║       • Customer (pelanggan)                                 ║");
        System.out.println("║       • Pabrik (produksi barang)                             ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static void tampilkanMenuUtama() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           📋 MENU UTAMA 📋           ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. 📦 Manajemen Produk             ║");
        System.out.println("║   2. 👥 Manajemen Customer (CRM)     ║");
        System.out.println("║   3. 🏭 Modul Pabrik                 ║");
        System.out.println("║   0. 🚪 Keluar Aplikasi              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Pilihan Anda: ");
    }

    private static int bacaPilihanUser() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            return -1;
        }
    }

    private static void tampilkanPerpisahan() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║       👋 TERIMA KASIH TELAH MENGGUNAKAN APLIKASI INI 👋      ║");
        System.out.println("║                                                              ║");
        System.out.println("║       Data Anda tersimpan aman di folder 'data/'             ║");
        System.out.println("║       Sampai jumpa lagi!                                     ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}
