package com.crm.project.features.pabrik;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.crm.project.features.produk.Product;
import com.crm.project.features.produk.ProductRepository;

public class ManufacturingUI {

    private final MaterialRepository repoMaterial;
    private final ProductRepository repoProduk;
    private final Scanner scanner;

    public ManufacturingUI() {
        this.repoMaterial = new MaterialRepository();
        this.repoProduk = new ProductRepository();
        this.scanner = new Scanner(System.in);
    }

    public void tampilkanMenu() {
        boolean lanjutkan = true;

        while (lanjutkan) {
            cetakHeader();
            int pilihan = bacaPilihan();

            switch (pilihan) {
                case 1 -> lihatStokBahan();
                case 2 -> belanjaBahan();
                case 3 -> prosesProduksi();
                case 4 -> tambahBahanBaru();
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
        System.out.println("║       🏭 MODUL PABRIK 🏭             ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║   1. Cek Stok Bahan Baku             ║");
        System.out.println("║   2. Belanja Bahan (Tambah Stok)     ║");
        System.out.println("║   3. ⚙️ PRODUKSI BARANG              ║");
        System.out.println("║   4. Tambah Jenis Bahan Baru         ║");
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

    private void lihatStokBahan() {
        System.out.println("\n📦 ═══════════════════ STOK BAHAN BAKU ═══════════════════");

        List<Material> daftarBahan = repoMaterial.ambilSemuaMaterial();

        if (daftarBahan.isEmpty()) {
            System.out.println("   📭 Belum ada bahan baku. Tambahkan dulu lewat menu 4.");
        } else {
            List<Material> bahanValid = new ArrayList<>();
            for (Material bahan : daftarBahan) {
                if (bahan.getNama() != null && !bahan.getNama().isEmpty()) {
                    bahanValid.add(bahan);
                }
            }

            if (bahanValid.isEmpty()) {
                System.out.println("   📭 Data bahan baku corrupt. Silakan tambahkan bahan baru lewat menu 4.");
            } else {
                System.out.println("┌───────┬───────────────────────────┬────────────┐");
                System.out.println("│  ID   │        Nama Bahan         │    Stok    │");
                System.out.println("├───────┼───────────────────────────┼────────────┤");

                for (Material bahan : bahanValid) {
                    System.out.println(bahan);
                }

                System.out.println("└───────┴───────────────────────────┴────────────┘");
                System.out.println("   📊 Total: " + bahanValid.size() + " jenis bahan");
            }
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }

    private void belanjaBahan() {
        System.out.println("\n🛒 ═══════════════════ BELANJA BAHAN ═══════════════════");

        List<Material> daftarBahan = repoMaterial.ambilSemuaMaterial();

        List<Material> bahanValid = new ArrayList<>();
        for (Material bahan : daftarBahan) {
            if (bahan.getNama() != null && !bahan.getNama().isEmpty()) {
                bahanValid.add(bahan);
            }
        }

        if (bahanValid.isEmpty()) {
            System.out.println("   📭 Belum ada bahan baku. Tambahkan dulu lewat menu 4.");
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("   Bahan yang tersedia:");
        for (Material bahan : bahanValid) {
            System.out.println(
                    "   • ID: " + bahan.getId() + " - " + bahan.getNama() + " (Stok: " + bahan.getStok() + ")");
        }

        int idBahan;
        try {
            System.out.print("\n   ID Bahan yang dibeli (0 untuk batal): ");
            idBahan = scanner.nextInt();
            if (idBahan == 0) {
                System.out.println("   🚫 Dibatalkan.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format ID tidak valid.\n");
            return;
        }

        Material bahan = repoMaterial.cariDenganId(idBahan);
        if (bahan == null) {
            System.out.println("   ❌ Bahan dengan ID " + idBahan + " tidak ditemukan.\n");
            return;
        }

        int jumlahBeli;
        try {
            System.out.print("   Jumlah yang dibeli: ");
            jumlahBeli = scanner.nextInt();
            if (jumlahBeli <= 0) {
                System.out.println("   ❌ Jumlah harus lebih dari 0.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format jumlah tidak valid.\n");
            return;
        }

        boolean berhasil = repoMaterial.tambahStok(idBahan, jumlahBeli);

        if (berhasil) {
            System.out.println("\n   ✅ Berhasil menambah " + jumlahBeli + " unit '" + bahan.getNama() + "'!");
            System.out.println("   📊 Stok baru: " + (bahan.getStok() + jumlahBeli));
        } else {
            System.out.println("\n   ❌ Gagal menambah stok.");
        }
        System.out.println("══════════════════════════════════════════════════════════\n");
    }

    private void prosesProduksi() {
        System.out.println("\n⚙️ ═══════════════════ PRODUKSI BARANG ═══════════════════");

        List<Material> daftarBahan = repoMaterial.ambilSemuaMaterial();

        List<Material> bahanValid = new ArrayList<>();
        for (Material bahan : daftarBahan) {
            if (bahan.getNama() != null && !bahan.getNama().isEmpty()) {
                bahanValid.add(bahan);
            }
        }

        if (bahanValid.isEmpty()) {
            System.out.println("   📭 Tidak ada bahan baku. Tambahkan dulu!");
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        List<Product> daftarProduk = repoProduk.ambilSemuaProduk();
        if (daftarProduk.isEmpty()) {
            System.out.println("   📭 Tidak ada produk. Tambahkan produk dulu di menu Produk!");
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        System.out.println("\n   📦 BAHAN BAKU TERSEDIA:");
        System.out.println("   ┌───────┬───────────────────────────┬────────────┐");
        System.out.println("   │  ID   │        Nama Bahan         │    Stok    │");
        System.out.println("   ├───────┼───────────────────────────┼────────────┤");
        for (Material bahan : bahanValid) {
            System.out.printf("   │ %-5d │ %-25s │ %-10d │%n",
                    bahan.getId(), bahan.getNama(), bahan.getStok());
        }
        System.out.println("   └───────┴───────────────────────────┴────────────┘");

        System.out.println("\n   📋 PRODUK YANG BISA DIPRODUKSI:");
        System.out.println("   ┌───────┬───────────────────────────┬────────────┐");
        System.out.println("   │  ID   │        Nama Produk        │  Stok Now  │");
        System.out.println("   ├───────┼───────────────────────────┼────────────┤");
        for (Product produk : daftarProduk) {
            System.out.printf("   │ %-5d │ %-25s │ %-10d │%n",
                    produk.getId(), produk.getNama(), produk.getStok());
        }
        System.out.println("   └───────┴───────────────────────────┴────────────┘");

        int idBahan;
        try {
            System.out.print("\n   ID Bahan yang digunakan (0 untuk batal): ");
            idBahan = scanner.nextInt();
            if (idBahan == 0) {
                System.out.println("   🚫 Produksi dibatalkan.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format ID tidak valid.\n");
            return;
        }

        Material bahanTerpilih = repoMaterial.cariDenganId(idBahan);
        if (bahanTerpilih == null) {
            System.out.println("   ❌ Bahan tidak ditemukan.\n");
            return;
        }

        int jumlahBahanPakai;
        try {
            System.out.print("   Jumlah bahan yang dipakai: ");
            jumlahBahanPakai = scanner.nextInt();
            if (jumlahBahanPakai <= 0) {
                System.out.println("   ❌ Jumlah harus positif.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format tidak valid.\n");
            return;
        }

        if (bahanTerpilih.getStok() < jumlahBahanPakai) {
            System.out.println("   ❌ Stok bahan tidak cukup! Tersedia: " + bahanTerpilih.getStok());
            System.out.println("══════════════════════════════════════════════════════════\n");
            return;
        }

        int idProduk;
        try {
            System.out.print("   ID Produk yang akan diproduksi: ");
            idProduk = scanner.nextInt();
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format ID tidak valid.\n");
            return;
        }

        Product produkTerpilih = repoProduk.cariDenganId(idProduk);
        if (produkTerpilih == null) {
            System.out.println("   ❌ Produk tidak ditemukan.\n");
            return;
        }

        int jumlahProduksi;
        try {
            System.out.print("   Jumlah produk yang dihasilkan: ");
            jumlahProduksi = scanner.nextInt();
            if (jumlahProduksi <= 0) {
                System.out.println("   ❌ Jumlah harus positif.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format tidak valid.\n");
            return;
        }

        System.out.println("\n   ⏳ Memproses produksi...");

        boolean bahanDikurangi = repoMaterial.kurangiStok(idBahan, jumlahBahanPakai);
        if (!bahanDikurangi) {
            System.out.println("   ❌ Gagal mengurangi stok bahan.\n");
            return;
        }

        boolean produkDitambah = repoProduk.tambahStok(idProduk, jumlahProduksi);
        if (!produkDitambah) {
            repoMaterial.tambahStok(idBahan, jumlahBahanPakai);
            System.out.println("   ❌ Gagal menambah stok produk. Bahan dikembalikan.\n");
            return;
        }

        System.out.println("\n   ╔═══════════════════════════════════════════════════════╗");
        System.out.println("   ║             ✅ PRODUKSI BERHASIL!                      ║");
        System.out.println("   ╠═══════════════════════════════════════════════════════╣");
        System.out.println("   ║   📉 Bahan '" + bahanTerpilih.getNama() + "' dikurangi: " + jumlahBahanPakai);
        System.out.println("   ║   📈 Produk '" + produkTerpilih.getNama() + "' ditambah: " + jumlahProduksi);
        System.out.println("   ╚═══════════════════════════════════════════════════════╝");
        System.out.println("══════════════════════════════════════════════════════════\n");
    }

    private void tambahBahanBaru() {
        System.out.println("\n➕ ═══════════════════ TAMBAH BAHAN BARU ═══════════════════");

        scanner.nextLine();

        System.out.print("   Nama Bahan: ");
        String namaBahan = scanner.nextLine().trim();

        if (namaBahan.isEmpty()) {
            System.out.println("   ❌ Nama bahan tidak boleh kosong.\n");
            return;
        }

        int stokAwal;
        try {
            System.out.print("   Stok Awal: ");
            stokAwal = scanner.nextInt();
            if (stokAwal < 0) {
                System.out.println("   ❌ Stok tidak boleh negatif.\n");
                return;
            }
        } catch (InputMismatchException kesalahan) {
            scanner.nextLine();
            System.out.println("   ❌ Format stok tidak valid.\n");
            return;
        }

        Material bahanBaru = new Material(namaBahan, stokAwal);
        boolean berhasil = repoMaterial.tambahMaterial(bahanBaru);

        if (berhasil) {
            System.out.println("\n   ✅ Bahan '" + namaBahan + "' berhasil ditambahkan!");
        } else {
            System.out.println("\n   ❌ Gagal menambahkan bahan.");
        }
        System.out.println("══════════════════════════════════════════════════════════════\n");
    }
}
