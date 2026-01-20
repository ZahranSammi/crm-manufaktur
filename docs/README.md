# Class Diagram Documentation - CRM Application

## 📋 Overview

Aplikasi CRM ini dibangun dengan arsitektur **3-Layer** (Model-Repository-UI) untuk memisahkan tanggung jawab setiap komponen. Semua data disimpan dalam format **JSON** menggunakan library **GSON**.

---

## 🏗️ Arsitektur Aplikasi

```
┌─────────────────────────────────────────┐
│          Main.java (Entry Point)        │
└─────────────────┬───────────────────────┘
                  │
        ┌─────────┼─────────┐
        │         │         │
        ▼         ▼         ▼
    ┌───────┐ ┌───────┐ ┌──────────────┐
    │Product│ │  CRM  │ │Manufacturing │
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

### 1. **com.crm.project.model** (Model Layer)

Berisi entity classes yang merepresentasikan data aplikasi.

#### 📄 Product.java
```
Atribut:
- id: int              → ID produk (auto-generated)
- name: String         → Nama produk
- price: double        → Harga produk
- stock: int           → Jumlah stok

Method:
+ Product()                                  → Constructor default
+ Product(name, price, stock)                → Constructor untuk produk baru
+ Product(id, name, price, stock)            → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getName(), setName(String)                 → Getter/Setter name
+ getPrice(), setPrice(double)               → Getter/Setter price
+ getStock(), setStock(int)                  → Getter/Setter stock
+ toString(): String                         → Format print table
```

**Fungsi:** Menyimpan informasi produk yang dijual/diproduksi.

---

#### 📄 Customer.java
```
Atribut:
- id: int              → ID customer (auto-generated)
- name: String         → Nama customer
- phone: String        → Nomor telepon

Method:
+ Customer()                                 → Constructor default
+ Customer(name, phone)                      → Constructor untuk customer baru
+ Customer(id, name, phone)                  → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getName(), setName(String)                 → Getter/Setter name
+ getPhone(), setPhone(String)               → Getter/Setter phone
+ toString(): String                         → Format print table
```

**Fungsi:** Menyimpan data pelanggan untuk modul CRM.

---

#### 📄 Material.java
```
Atribut:
- id: int              → ID material (auto-generated)
- name: String         → Nama bahan baku
- stock: int           → Jumlah stok bahan

Method:
+ Material()                                 → Constructor default
+ Material(name, stock)                      → Constructor untuk material baru
+ Material(id, name, stock)                  → Constructor lengkap
+ getId(), setId(int)                        → Getter/Setter ID
+ getName(), setName(String)                 → Getter/Setter name
+ getStock(), setStock(int)                  → Getter/Setter stock
+ toString(): String                         → Format print table
```

**Fungsi:** Menyimpan data bahan baku untuk proses produksi.

---

### 2. **com.zahran.project.repository** (Repository Layer)

Berisi logic untuk operasi CRUD (Create, Read, Update, Delete) dengan JSON file storage.

#### 📄 ProductRepository.java
```
Atribut:
- DATA_FILE: String = "data/products.json"   → Path file penyimpanan
- gson: Gson                                 → GSON instance untuk serialisasi

Method:
+ getAllProducts(): List<Product>            → Baca semua produk dari JSON
+ addProduct(product: Product): boolean      → Tambah produk baru (auto-generate ID)
+ deleteProduct(id: int): boolean            → Hapus produk berdasarkan ID
+ findById(id: int): Product                 → Cari produk by ID
+ updateProduct(product: Product): boolean   → Update data produk
+ addStock(id: int, amount: int): boolean    → Tambah stok produk (untuk produksi)

Private Method:
- saveProducts(products: List<Product>)      → Simpan ke JSON file
- initializeDataFile()                       → Buat file jika belum ada
- getNextId(): int                           → Generate ID berikutnya
```

**Fungsi:** Mengelola data produk dengan file `products.json`.

**File JSON Format:**
```json
[
  {
    "id": 1,
    "name": "Laptop ASUS",
    "price": 12500000.0,
    "stock": 10
  }
]
```

---

#### 📄 CustomerRepository.java
```
Atribut:
- DATA_FILE: String = "data/customers.json"
- gson: Gson

Method:
+ getAllCustomers(): List<Customer>          → Baca semua customer
+ addCustomer(customer: Customer): boolean   → Tambah customer baru
+ deleteCustomer(id: int): boolean           → Hapus customer
+ findById(id: int): Customer                → Cari customer by ID

Private Method:
- saveCustomers(customers: List<Customer>)
- initializeDataFile()
- getNextId(): int
```

**Fungsi:** Mengelola data customer dengan file `customers.json`.

---

#### 📄 MaterialRepository.java
```
Atribut:
- DATA_FILE: String = "data/materials.json"
- gson: Gson

Method:
+ getAllMaterials(): List<Material>          → Baca semua material
+ addMaterial(material: Material): boolean   → Tambah material baru
+ deleteMaterial(id: int): boolean           → Hapus material
+ findById(id: int): Material                → Cari material by ID
+ reduceStock(id: int, amount: int): boolean → Kurangi stok (untuk produksi)
+ addStock(id: int, amount: int): boolean    → Tambah stok (belanja bahan)

Private Method:
- saveMaterials(materials: List<Material>)
- initializeDataFile()
- getNextId(): int
```

**Fungsi:** Mengelola data bahan baku dengan file `materials.json`.

**Method Khusus:**
- `reduceStock()` → Digunakan saat produksi untuk mengurangi bahan
- `addStock()` → Digunakan saat belanja bahan baku

---

### 3. **com.zahran.project.ui** & **com.zahran.project.modules.*.ui** (UI Layer)

Berisi interface berbasis CLI (Scanner) untuk interaksi dengan user.

#### 📄 ProductUI.java
```
Atribut:
- productRepository: ProductRepository       → Instance repository
- scanner: Scanner                           → Input handler

Method:
+ showMenu(): void                           → Loop menu utama
- printMenuHeader(): void                    → Print header menu
- getMenuChoice(): int                       → Baca pilihan user (dengan validasi)
- showAllProducts(): void                    → Tampilkan semua produk dalam table
- addNewProduct(): void                      → Form tambah produk baru
- deleteProduct(): void                      → Form hapus produk
```

**Menu Options:**
1. Show All Products
2. Add New Product
3. Delete Product
0. Back to Main Menu

---

#### 📄 CrmUI.java (modules/crm/ui/)
```
Atribut:
- customerRepository: CustomerRepository
- scanner: Scanner

Method:
+ showMenu(): void
- printMenuHeader(): void
- getMenuChoice(): int
- showAllCustomers(): void                   → Tampilkan semua customer
- addNewCustomer(): void                     → Form tambah customer
- deleteCustomer(): void                     → Form hapus customer
```

**Menu Options:**
1. Show All Customers
2. Add New Customer
3. Delete Customer
0. Back to Main Menu

---

#### 📄 ManufacturingUI.java (modules/manufacturing/ui/)
```
Atribut:
- materialRepository: MaterialRepository     → Untuk kelola material
- productRepository: ProductRepository       → Untuk update stok produk hasil produksi
- scanner: Scanner

Method:
+ showMenu(): void
- printMenuHeader(): void
- getMenuChoice(): int
- checkMaterialStock(): void                 → Lihat stok bahan baku
- addMaterialStock(): void                   → Belanja/tambah stok bahan
- addNewMaterial(): void                     → Tambah jenis bahan baru
- produceProduct(): void                     → Proses produksi (logika utama)
```

**Menu Options:**
1. Cek Stok Bahan
2. Belanja Bahan (Tambah Stok)
3. PRODUKSI BARANG
4. Tambah Bahan Baru
0. Back to Main Menu

**Logika Produksi:**
```
1. Tampilkan list materials & products
2. User pilih material ID & jumlah yang dipakai
3. User pilih product ID yang akan diproduksi & quantity
4. Validasi: Apakah stok material cukup?
   - Jika YA  → Kurangi material.stock, Tambah product.stock
   - Jika NO  → Tampilkan error "Stok tidak cukup"
5. Simpan perubahan ke kedua file JSON
6. Rollback jika ada error
```

---

### 4. **com.example.Main** (Entry Point)

#### 📄 Main.java
```
Atribut:
- scanner: Scanner (static)                  → Scanner untuk main menu

Method:
+ main(args: String[]): void                 → Entry point aplikasi
  - Tampilkan welcome message
  - Loop menu utama
  - Route ke modul yang dipilih
  - Exit message

- printMainMenu(): void                      → Print main menu
- getMenuChoice(): int                       → Baca pilihan user
```

**Main Menu Options:**
1. Product Management → new ProductUI().showMenu()
2. CRM (Customer Management) → new CrmUI().showMenu()
3. Manufacturing → new ManufacturingUI().showMenu()
0. Exit → Keluar aplikasi

---

## 🔗 Relationship Diagram

### Dependencies

```
Main
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

### Use Case Flow

**1. Tambah Product:**
```
User → ProductUI.addNewProduct()
     → ProductRepository.addProduct()
     → Save to products.json
```

**2. Tambah Customer:**
```
User → CrmUI.addNewCustomer()
     → CustomerRepository.addCustomer()
     → Save to customers.json
```

**3. Produksi (Manufacturing):**
```
User → ManufacturingUI.produceProduct()
     ├─→ MaterialRepository.reduceStock()  → Update materials.json
     └─→ ProductRepository.addStock()      → Update products.json
```

---

## 💾 Data Persistence

Semua data disimpan dalam format **JSON** di folder `data/`:

| File | Content | Auto-Created |
|------|---------|--------------|
| `data/products.json` | Array of Product objects | ✅ |
| `data/customers.json` | Array of Customer objects | ✅ |
| `data/materials.json` | Array of Material objects | ✅ |

**Keuntungan JSON Storage:**
- ✅ Tidak perlu database server
- ✅ Human-readable format
- ✅ Mudah di-backup
- ✅ Portable (bisa pindah file)
- ✅ GSON handle serialization/deserialization otomatis

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

### 3. **Dependency Injection (Manual)**
```java
public class ProductUI {
    private final ProductRepository productRepository;
    
    public ProductUI() {
        this.productRepository = new ProductRepository(); // Inject dependency
    }
}
```

---

## 🔄 Data Flow Example

### Scenario: User ingin produksi 1 laptop dari 5 unit komponen

```
1. User memilih menu Manufacturing (3)
   ↓
2. Pilih "Produksi Barang" (3)
   ↓
3. Sistem menampilkan:
   Materials: [ID:1, Komponen, Stock:10]
   Products:  [ID:1, Laptop, Stock:5]
   ↓
4. User input:
   - Material ID: 1
   - Amount to use: 5
   - Product ID: 1
   - Production qty: 1
   ↓
5. Sistem execute:
   materialRepo.reduceStock(1, 5)  → Stock komponen: 10 - 5 = 5
   productRepo.addStock(1, 1)       → Stock laptop: 5 + 1 = 6
   ↓
6. Save both JSON files
   ↓
7. Tampilkan success message
```

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
| `Main` | Controller | Application entry & routing |

---

## 🚀 Extension Points

Jika ingin menambah fitur baru, ikuti pattern yang sama:

1. **Tambah Model** di `com.zahran.project.model`
2. **Tambah Repository** di `com.zahran.project.repository`
3. **Tambah UI** di `com.zahran.project.modules.*.ui`
4. **Update Main.java** untuk routing

Contoh: Jika ingin tambah modul **Supplier**:
```
Supplier.java (model)
SupplierRepository.java (repository)
SupplierUI.java (ui)
Main.java → case 4: new SupplierUI().showMenu()
```

---

## ✅ Best Practices Applied

1. ✅ **Single Responsibility Principle** - Setiap class punya 1 tanggung jawab
2. ✅ **DRY (Don't Repeat Yourself)** - Code reuse di repository base methods
3. ✅ **Encapsulation** - Fields private, akses via getters/setters
4. ✅ **Error Handling** - Try-catch di semua file I/O operations
5. ✅ **Input Validation** - Validasi user input di UI layer
6. ✅ **Consistent Naming** - Naming convention konsisten
7. ✅ **Code Documentation** - JavaDoc comments di method penting

---

**Last Updated:** 2026-01-21  
**Author:** Zahran  
**Project:** CRM Application with Manufacturing Module
