package com.crm.project.features.produk;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProductUI {

    private final ProductRepository repository;
    private final Scanner scanner;

    public ProductUI() {
        this.repository = new ProductRepository();
        this.scanner = new Scanner(System.in);
    }

    public void tampilkanMenu() {
        boolean lanjutkan = true;

        while (lanjutkan) {
            cetakHeader();
            int pilihan = bacaPilihan();

            switch (pilihan) {
                case 1 -> lihatSemuaProduk();
                case 2 -> tambahProdukBaru();
                case 3 -> hapusProduk();
                case 0 -> {
                    lanjutkan = false;
                    System.out.println("\n🔙 Kembali ke Menu Utama...\n");
                }
                default -> System.out.println("\n⚠️ Pilihan tidak valid. Masukkan 0-3.\n");
            }
        }
    }

    private void cetakHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       📦 MANAJEMEN PRODUK 📦         ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. Lihat Semua Produk              ║");
        System.out.println("║   2. Tambah Produk Baru              ║");
        System.out.println("║   3. Hapus Produk                    ║");
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

    private void lihatSemuaProduk() {
        System.out.println("\n📦 ═══════════════════ DAFTAR PRODUK ═══════════════════");

        List<Product> daftarProduk = repository.ambilSemuaProduk();

        if (daftarProduk.isEmpty()) {
            System.out.println("   📭 Belum ada produk tersimpan.");
        } else {
            System.out.println("┌───────┬───────────────────────────┬────────────────┬──────────┐");
            System.out.println("│  ID   │           Nama            │     Harga      │   Stok   │");
            System.out.println("├───────┼───────────────────────────┼────────────────┼──────────┤");

            for (Product produk : daftarProduk) {
                System.out.println(produk);
            }

            System.out.println("└───────┴───────────────────────────┴────────────────┴──────────┘");
            System.out.println("   📊 Total: " + daftarProduk.size() + " produk");
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }

    private void tambahProdukBaru() {
        System.out.println("\n➕ ═══════════════════ TAMBAH PRODUK BARU ═══════════════════");

        scanner.nextLine();

        System.out.print("   Nama Produk: ");
        String namaProduk = scanner.nextLine().trim();

        if (namaProduk.isEmpty()) {
            System.out.println("   ❌ Error: Nama produk tidak boleh kosong.\n");
            return;
        }

        double hargaProduk;
        try {
            System.out.print("   Harga (Rp): ");
            hargaProduk = scanner.nextDouble();
            if (hargaProduk < 0) {
                System.out.println("   ❌ Error: Harga tidak boleh negatif.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Format harga tidak valid.\n");
            return;
        }

        int stokProduk;
        try {
            System.out.print("   Stok Awal: ");
            stokProduk = scanner.nextInt();
            if (stokProduk < 0) {
                System.out.println("   ❌ Error: Stok tidak boleh negatif.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Error: Format stok tidak valid.\n");
            return;
        }

        Product produkBaru = new Product(namaProduk, hargaProduk, stokProduk);
        boolean berhasil = repository.tambahProduk(produkBaru);

        if (berhasil) {
            System.out.println("\n   ✅ Produk '" + namaProduk + "' berhasil ditambahkan!");
        } else {
            System.out.println("\n   ❌ Gagal menambahkan produk. Silakan coba lagi.");
        }
        System.out.println("══════════════════════════════════════════════════════════════\n");
    }

    private void hapusProduk() {
        System.out.println("\n🗑️ ═══════════════════ HAPUS PRODUK ═══════════════════");

        List<Product> daftarProduk = repository.ambilSemuaProduk();

        if (daftarProduk.isEmpty()) {
            System.out.println("   📭 Tidak ada produk untuk dihapus.");
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Produk yang tersedia:");
        for (Product produk : daftarProduk) {
            System.out.println("   • ID: " + produk.getId() + " - " + produk.getNama());
        }

        int idHapus;
        try {
            System.out.print("\n   Masukkan ID produk yang akan dihapus (0 untuk batal): ");
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

        System.out.print("   ⚠️ Yakin ingin menghapus produk ID " + idHapus + "? (y/n): ");
        scanner.nextLine();
        String konfirmasi = scanner.nextLine().trim().toLowerCase();

        if (!konfirmasi.equals("y") && !konfirmasi.equals("ya")) {
            System.out.println("   🚫 Penghapusan dibatalkan.\n");
            return;
        }

        boolean berhasil = repository.hapusProduk(idHapus);

        if (berhasil) {
            System.out.println("\n   ✅ Produk berhasil dihapus!");
        } else {
            System.out.println("\n   ❌ Gagal menghapus. Produk dengan ID " + idHapus + " tidak ditemukan.");
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }
}
