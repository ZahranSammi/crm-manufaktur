# Class Diagram Documentation - CRM Application

## 📋 Overview

Aplikasi CRM ini dibangun dengan arsitektur **3-Layer** (Model-Repository-UI) untuk memisahkan tanggung jawab setiap komponen. Semua data disimpan dalam format **JSON** menggunakan library **GSON**.

---

## 🏗️ Arsitektur Aplikasi

```
┌─────────────────────────────────────────┐
│         MainApp.java (Entry Point)       │
└─────────────────┬───────────────────────┘
                  │
        ┌─────────┼─────────┐
        │         │         │
        ▼         ▼         ▼
    ┌───────┐ ┌───────┐ ┌──────────────┐
    │Product│ │  Crm  │ │Manufacturing │
    │  UI   │ │  UI   │ │     UI       │
    └───┬───┘ └───┬───┘ └──────┬───────┘
        │         │            │
        ▼         ▼            ▼
    ┌────────┐ ┌────────┐ ┌──────────┐
    │Product │ │Customer│ │ Material │
    │  Repo  │ │  Repo  │ │   Repo   │
    └───┬────┘ └───┬────┘ └────┬─────┘
        │          │            │
        ▼          ▼            ▼
    ┌────────┐ ┌────────┐ ┌──────────┐
    │Product │ │Customer│ │ Material │
    │ Model  │ │ Model  │ │  Model   │
    └────────┘ └────────┘ └──────────┘
```

---

## 📦 Package Structure

### 1. **com.crm.project.features.produk** (Product Feature)

#### 📄 Product.java
```
Atribut:
- id: int              → ID produk (auto-generated)
- nama: String         → Nama produk
- harga: double        → Harga produk
- stok: int            → Jumlah stok

Method:
+ Product()                                  → Constructor default
+ Product(nama, harga, stok)                 → Constructor untuk produk baru
+ Product(id, nama, harga, stok)             → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getNama(), setNama(String)                 → Getter/Setter nama
+ getHarga(), setHarga(double)               → Getter/Setter harga
+ getStok(), setStok(int)                    → Getter/Setter stok
+ toString(): String                         → Format print table
```

---

#### 📄 ProductRepository.java
```
Atribut:
- FILE_PRODUK: String = "data/products.json"   → Path file penyimpanan
- gson: Gson                                   → GSON instance untuk serialisasi

Method:
+ ambilSemuaProduk(): List<Product>            → Baca semua produk dari JSON
+ tambahProduk(product: Product): boolean      → Tambah produk baru (auto-generate ID)
+ hapusProduk(id: int): boolean                → Hapus produk berdasarkan ID
+ cariDenganId(id: int): Product               → Cari produk by ID
+ updateProduk(product: Product): boolean      → Update data produk
+ tambahStok(id: int, jumlah: int): boolean    → Tambah stok produk (untuk produksi)

Private Method:
- simpanSemuaProduk(products: List<Product>)   → Simpan ke JSON file
- inisialisasiFile()                           → Buat file jika belum ada
- ambilIdBerikutnya(): int                     → Generate ID berikutnya
```

---

#### 📄 ProductUI.java
```
Atribut:
- repository: ProductRepository              → Instance repository
- scanner: Scanner                           → Input handler

Method:
+ tampilkanMenu(): void                      → Loop menu utama
- cetakHeader(): void                        → Print header menu
- bacaPilihan(): int                         → Baca pilihan user (dengan validasi)
- lihatSemuaProduk(): void                   → Tampilkan semua produk dalam table
- tambahProdukBaru(): void                   → Form tambah produk baru
- hapusProduk(): void                        → Form hapus produk
```

---

### 2. **com.crm.project.features.crm** (Customer Feature)

#### 📄 Customer.java
```
Atribut:
- id: int              → ID customer (auto-generated)
- nama: String         → Nama customer
- telepon: String      → Nomor telepon

Method:
+ Customer()                                 → Constructor default
+ Customer(nama, telepon)                    → Constructor untuk customer baru
+ Customer(id, nama, telepon)                → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getNama(), setNama(String)                 → Getter/Setter nama
+ getTelepon(), setTelepon(String)           → Getter/Setter telepon
+ toString(): String                         → Format print table
```

---

#### 📄 CustomerRepository.java
```
Atribut:
- FILE_CUSTOMER: String = "data/customers.json"
- gson: Gson

Method:
+ ambilSemuaCustomer(): List<Customer>       → Baca semua customer
+ tambahCustomer(customer: Customer): boolean → Tambah customer baru
+ hapusCustomer(id: int): boolean            → Hapus customer
+ cariDenganId(id: int): Customer            → Cari customer by ID
```

---

#### 📄 CrmUI.java
```
Atribut:
- repository: CustomerRepository
- scanner: Scanner

Method:
+ tampilkanMenu(): void
- cetakHeader(): void
- bacaPilihan(): int
- lihatSemuaCustomer(): void                 → Tampilkan semua customer
- tambahCustomerBaru(): void                 → Form tambah customer
- hapusCustomer(): void                      → Form hapus customer
```

---

### 3. **com.crm.project.features.pabrik** (Manufacturing Feature)

#### 📄 Material.java
```
Atribut:
- id: int              → ID material (auto-generated)
- nama: String         → Nama bahan baku
- stok: int            → Jumlah stok bahan

Method:
+ Material()                                 → Constructor default
+ Material(nama, stok)                       → Constructor untuk material baru
+ Material(id, nama, stok)                   → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getNama(), setNama(String)                 → Getter/Setter nama
+ getStok(), setStok(int)                    → Getter/Setter stok
+ toString(): String                         → Format print table
```

---

#### 📄 MaterialRepository.java
```
Atribut:
- FILE_MATERIAL: String = "data/materials.json"
- gson: Gson

Method:
+ ambilSemuaMaterial(): List<Material>       → Baca semua material
+ tambahMaterial(material: Material): boolean → Tambah material baru
+ hapusMaterial(id: int): boolean            → Hapus material
+ cariDenganId(id: int): Material            → Cari material by ID
+ kurangiStok(id: int, jumlah: int): boolean → Kurangi stok (untuk produksi)
+ tambahStok(id: int, jumlah: int): boolean  → Tambah stok (belanja bahan)
```

---

#### 📄 ManufacturingUI.java
```
Atribut:
- repoMaterial: MaterialRepository           → Untuk kelola material
- repoProduk: ProductRepository              → Untuk update stok produk hasil produksi
- scanner: Scanner

Method:
+ tampilkanMenu(): void
- cetakHeader(): void
- bacaPilihan(): int
- lihatStokBahan(): void                     → Lihat stok bahan baku
- belanjaBahan(): void                       → Belanja/tambah stok bahan
- tambahBahanBaru(): void                    → Tambah jenis bahan baru
- prosesProduksi(): void                     → Proses produksi (logika utama)
```

**Menu Options:**
1. Cek Stok Bahan
2. Belanja Bahan (Tambah Stok)
3. PRODUKSI BARANG
4. Tambah Bahan Baru
0. Kembali ke Menu Utama

---

### 4. **com.crm.project** (Entry Point)

#### 📄 MainApp.java
```
Atribut:
- scanner: Scanner (static)                  → Scanner untuk main menu

Method:
+ main(args: String[]): void                 → Entry point aplikasi
- tampilkanSambutan(): void                  → Tampilkan welcome message
- tampilkanMenuUtama(): void                 → Print main menu
- bacaPilihanUser(): int                     → Baca pilihan user
- tampilkanPerpisahan(): void                → Exit message
```

**Main Menu Options:**
1. Manajemen Produk → new ProductUI().tampilkanMenu()
2. CRM (Customer Management) → new CrmUI().tampilkanMenu()
3. Modul Pabrik → new ManufacturingUI().tampilkanMenu()
0. Keluar Aplikasi

---

## 🔗 Relationship Diagram

### Dependencies

```
MainApp
 ├─→ ProductUI
 │    └─→ ProductRepository
 │         └─→ Product (model)
 │
 ├─→ CrmUI
 │    └─→ CustomerRepository
 │         └─→ Customer (model)
 │
 └─→ ManufacturingUI
      ├─→ MaterialRepository
      │    └─→ Material (model)
      └─→ ProductRepository
           └─→ Product (model)
```

---

## 💾 Data Persistence

Semua data disimpan dalam format **JSON** di folder `data/`:

| File | Content | Auto-Created |
|------|---------|--------------|
| `data/products.json` | Array of Product objects | ✅ |
| `data/customers.json` | Array of Customer objects | ✅ |
| `data/materials.json` | Array of Material objects | ✅ |

---

## 🎯 Design Patterns Used

### 1. **Repository Pattern**
Memisahkan logic akses data dari business logic.
```
UI ← → Repository ← → JSON File
```

### 2. **Separation of Concerns**
- Model: Data structure
- Repository: Data access
- UI: User interaction

---

## 📊 Class Responsibilities Summary

| Class | Type | Responsibility |
|-------|------|----------------|
| `Product` | Model | Hold product data |
| `Customer` | Model | Hold customer data |
| `Material` | Model | Hold material data |
| `ProductRepository` | Repository | CRUD operations for products |
| `CustomerRepository` | Repository | CRUD operations for customers |
| `MaterialRepository` | Repository | CRUD operations + stock management for materials |
| `ProductUI` | UI | Product management interface |
| `CrmUI` | UI | Customer management interface |
| `ManufacturingUI` | UI | Material & production interface |
| `MainApp` | Controller | Application entry & routing |

---

**Last Updated:** 2026-01-21  
**Author:** Zahran  
**Project:** CRM Application with Manufacturing Module
