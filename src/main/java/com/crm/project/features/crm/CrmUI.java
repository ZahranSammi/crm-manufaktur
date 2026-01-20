package com.crm.project.features.crm;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CrmUI {

    private final CustomerRepository repository;
    private final Scanner scanner;

    public CrmUI() {
        this.repository = new CustomerRepository();
        this.scanner = new Scanner(System.in);
    }

    public void tampilkanMenu() {
        boolean lanjutkan = true;

        while (lanjutkan) {
            cetakHeader();
            int pilihan = bacaPilihan();

            switch (pilihan) {
                case 1 -> lihatSemuaCustomer();
                case 2 -> tambahCustomerBaru();
                case 3 -> hapusCustomer();
                case 0 -> {
                    lanjutkan = false;
                    System.out.println("\n🔙 Kembali ke Menu Utama...\n");
                }
                default -> System.out.println("\n⚠️ Pilihan tidak valid.\n");
            }
        }
    }

    private void cetakHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       👥 MANAJEMEN CUSTOMER 👥       ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. Lihat Semua Customer            ║");
        System.out.println("║   2. Tambah Customer Baru            ║");
        System.out.println("║   3. Hapus Customer                  ║");
        System.out.println("║   0. Kembali ke Menu Utama           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Pilihan Anda: ");
    }

    private int bacaPilihan() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            return -1;
        }
    }

    private void lihatSemuaCustomer() {
        System.out.println("\n👥 ═══════════════════ DAFTAR CUSTOMER ═══════════════════");

        List<Customer> daftarCustomer = repository.ambilSemuaCustomer();

        if (daftarCustomer.isEmpty()) {
            System.out.println("   📭 Belum ada customer tersimpan.");
        } else {
            System.out.println("┌───────┬───────────────────────────┬─────────────────┐");
            System.out.println("│  ID   │           Nama            │     Telepon     │");
            System.out.println("├───────┼───────────────────────────┼─────────────────┤");

            for (Customer customer : daftarCustomer) {
                System.out.println(customer);
            }

            System.out.println("└───────┴───────────────────────────┴─────────────────┘");
            System.out.println("   📊 Total: " + daftarCustomer.size() + " customer");
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }

    private void tambahCustomerBaru() {
        System.out.println("\n➕ ═══════════════════ TAMBAH CUSTOMER BARU ═══════════════════");

        scanner.nextLine();

        System.out.print("   Nama Customer: ");
        String namaCustomer = scanner.nextLine().trim();

        if (namaCustomer.isEmpty()) {
            System.out.println("   ❌ Error: Nama tidak boleh kosong.\n");
            return;
        }

        System.out.print("   Nomor Telepon: ");
        String teleponCustomer = scanner.nextLine().trim();

        if (teleponCustomer.isEmpty()) {
            System.out.println("   ❌ Error: Telepon tidak boleh kosong.\n");
            return;
        }

        Customer customerBaru = new Customer(namaCustomer, teleponCustomer);
        boolean berhasil = repository.tambahCustomer(customerBaru);

        if (berhasil) {
            System.out.println("\n   ✅ Customer '" + namaCustomer + "' berhasil ditambahkan!");
        } else {
            System.out.println("\n   ❌ Gagal menambahkan customer.");
        }
        System.out.println("══════════════════════════════════════════════════════════════\n");
    }

    private void hapusCustomer() {
        System.out.println("\n🗑️ ═══════════════════ HAPUS CUSTOMER ═══════════════════");

        List<Customer> daftarCustomer = repository.ambilSemuaCustomer();

        if (daftarCustomer.isEmpty()) {
            System.out.println("   📭 Tidak ada customer untuk dihapus.");
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Customer yang tersedia:");
        for (Customer customer : daftarCustomer) {
            System.out.println("   • ID: " + customer.getId() + " - " + customer.getNama());
        }

        int idHapus;
        try {
            System.out.print("\n   Masukkan ID customer yang akan dihapus (0 untuk batal): ");
            idHapus = scanner.nextInt();

            if (idHapus == 0) {
                System.out.println("   🚫 Penghapusan dibatalkan.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Format ID tidak valid.\n");
            return;
        }

        System.out.print("   ⚠️ Yakin ingin menghapus customer ID " + idHapus + "? (y/n): ");
        scanner.nextLine();
        String konfirmasi = scanner.nextLine().trim().toLowerCase();

        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("   🚫 Penghapusan dibatalkan.\n");
            return;
        }

        boolean berhasil = repository.hapusCustomer(idHapus);

        if (berhasil) {
            System.out.println("\n   ✅ Customer berhasil dihapus!");
        } else {
            System.out.println("\n   ❌ Customer dengan ID " + idHapus + " tidak ditemukan.");
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }
}
