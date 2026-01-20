# 🏢 Sistem Manajemen Toko - Student Friendly Guide

## 📖 Penjelasan Singkat

Aplikasi ini adalah **Sistem Manajemen Toko** yang bisa mengelola:
- **Produk** (barang dagangan)
- **Customer** (data pelanggan)
- **Pabrik** (produksi barang dari bahan baku)

---

## 🎯 Analogi Sederhana: Bayangkan Sebuah Hotel!

Agar mudah memahami struktur kode ini, bayangkan aplikasi ini seperti sebuah **Hotel**:

```
🏢 HOTEL (Aplikasi)
│
├── 🛎️ LOBI (MainApp.java)
│   Tempat pertama yang dikunjungi tamu.
│   Resepsionis mengarahkan ke ruangan yang diinginkan.
│   TIDAK menyimpan barang, hanya navigasi.
│
├── 📦 RUANG PRODUK (features/produk/)
│   Ruangan khusus untuk urusan barang dagangan.
│   Ada meja resepsionis (UI), gudang (Repository), dan barang (Model).
│
├── 👥 RUANG CUSTOMER (features/crm/)
│   Ruangan khusus untuk urusan pelanggan.
│   Menyimpan data member dan melayani pendaftaran.
│
├── 🏭 RUANG PABRIK (features/pabrik/)
│   Ruangan khusus untuk produksi.
│   Bahan baku masuk → Diproses → Produk jadi keluar.
│
└── 🗄️ GUDANG ARSIP (database/)
    Tempat menyimpan semua data (file JSON).
    Petugas arsip bantu semua ruangan simpan/ambil data.
```

---

## 📁 Struktur Folder

```
src/main/java/com/zahran/project/
│
├── MainApp.java                     ← Pintu masuk aplikasi (Lobi)
│
├── database/
│   └── JsonDatabase.java            ← Helper untuk baca/tulis file JSON
│
└── features/
    │
    ├── produk/                      ← Semua tentang Produk
    │   ├── Product.java             ← Model (data produk)
    │   ├── ProductRepository.java   ← Gudang (simpan/ambil data)
    │   └── ProductUI.java           ← Menu tampilan
    │
    ├── crm/                         ← Semua tentang Customer
    │   ├── Customer.java
    │   ├── CustomerRepository.java
    │   └── CrmUI.java
    │
    └── pabrik/                      ← Semua tentang Produksi
        ├── Material.java            ← Model bahan baku
        ├── MaterialRepository.java  ← Gudang bahan
        └── ManufacturingUI.java     ← Menu pabrik + logika produksi
```

---

## 🏗️ Konsep OOP yang Digunakan

### 1. Encapsulation (Enkapsulasi)
Semua data di-protect dengan `private`, akses lewat `getter/setter`.
```java
private String nama;  // ❌ Tidak bisa diakses langsung
public String getNama() { return nama; }  // ✅ Harus lewat sini
```

### 2. Separation of Concerns (Pemisahan Tugas)
Setiap class punya tugas spesifik:
| Class | Tugas |
|-------|-------|
| Model | Menyimpan data |
| Repository | Baca/tulis ke file |
| UI | Tampilan dan interaksi user |

### 3. Package by Feature
File dikelompokkan berdasarkan **FITUR**, bukan **JENIS FILE**.
- ✅ `features/produk/` → Product, ProductRepository, ProductUI
- ❌ `model/Product`, `repository/ProductRepo`, `ui/ProductUI`

---

## 🚀 Cara Menjalankan

### Via IntelliJ IDEA:
1. Buka file `MainApp.java`
2. Klik tombol ▶️ (Run) atau tekan **Shift + F10**

### Via Terminal:
```bash
cd c:\Users\zahra\IdeaProjects\crm
mvn clean compile
mvn exec:java -Dexec.mainClass="com.zahran.project.MainApp"
```

---

## 📊 Alur Data

### Menambah Produk Baru:
```
User ketik nama & harga
       ↓
ProductUI.tambahProdukBaru()
       ↓
ProductRepository.tambahProduct()
       ↓
JsonDatabase.simpanSemuaData()
       ↓
💾 Tersimpan di data/products.json
```

### Proses Produksi:
```
User pilih bahan & produk
       ↓
ManufacturingUI.prosesProduksi()
       ↓
MaterialRepository.kurangiStok()  →  Bahan berkurang
ProductRepository.tambahStok()    →  Produk bertambah
       ↓
💾 Kedua file JSON di-update
```

---

## 📝 Data Storage

Semua data disimpan di folder `data/`:
- `data/products.json` → Daftar produk
- `data/customers.json` → Daftar customer
- `data/materials.json` → Daftar bahan baku

Format JSON (mudah dibaca manusia):
```json
[
  {
    "id": 1,
    "nama": "Laptop ASUS",
    "harga": 15000000,
    "stok": 10
  }
]
```

---

## ✨ Clean Code yang Diterapkan

| Rule | Contoh |
|------|--------|
| Komentar Bahasa Indonesia | `/** Menambah produk baru ke gudang */` |
| Nama variabel jelas | `daftarProduk` bukan `list` |
| Nama variabel Bahasa | `namaCustomer` bukan `name` |
| MaxApp = Navigasi saja | Tidak ada CRUD di MainApp |

---

## 👨‍💻 Author

**Zahran** - 2026

*Dibuat untuk pembelajaran OOP dan Clean Code*