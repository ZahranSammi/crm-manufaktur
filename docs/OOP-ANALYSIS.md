# Class Diagram & OOP Concepts - CRM Application

## 📊 Class Diagram Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                      MAINAPP (Entry Point)                       │
│  - scanner: Scanner                                             │
│  + main(String[]): void                                         │
└────────────────┬─────────────┬──────────────────────────────────┘
                 │             │
       «creates» │             │ «creates»
                 │             │
    ┌────────────▼─────┐  ┌───▼──────────┐  ┌────────────────────┐
    │    ProductUI     │  │    CrmUI     │  │  ManufacturingUI   │
    ├──────────────────┤  ├──────────────┤  ├────────────────────┤
    │ -repository      │  │ -repository  │  │ -repoMaterial      │
    │ -scanner         │  │ -scanner     │  │ -repoProduk        │
    ├──────────────────┤  ├──────────────┤  │ -scanner           │
    │ +tampilkanMenu() │  │ +tampilkan   │  ├────────────────────┤
    └────────┬─────────┘  │  Menu()      │  │ +tampilkanMenu()   │
             │            └──────┬───────┘  │ +prosesProduksi()  │
             │ «uses»            │ «uses»   └─────┬──────┬───────┘
             │                   │                │      │
             │                   │       «uses»   │      │ «uses»
             │                   │                │      │
    ┌────────▼─────────┐  ┌──────▼──────────┐  ┌──▼──────▼─────┐
    │ ProductRepository│  │CustomerRepository│  │MaterialRepository│
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ -FILE_PRODUK     │  │ -FILE_CUSTOMER  │  │ -FILE_MATERIAL   │
    │ -gson: Gson      │  │ -gson: Gson     │  │ -gson: Gson      │
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ +ambilSemuaProduk│  │ +ambilSemua     │  │ +ambilSemua      │
    │ +tambahProduk()  │  │  Customer()     │  │  Material()      │
    │ +hapusProduk()   │  │ +tambahCustomer │  │ +kurangiStok()   │
    │ +tambahStok()    │  │ +cariDenganId() │  │ +tambahStok()    │
    └────────┬─────────┘  └────────┬────────┘  └──────┬───────────┘
             │                     │                   │
             │ «manages»           │ «manages»         │ «manages»
             │                     │                   │
    ┌────────▼─────────┐  ┌────────▼────────┐  ┌──────▼───────────┐
    │     Product      │  │    Customer     │  │     Material     │
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ -id: int         │  │ -id: int        │  │ -id: int         │
    │ -nama: String    │  │ -nama: String   │  │ -nama: String    │
    │ -harga: double   │  │ -telepon: String│  │ -stok: int       │
    │ -stok: int       │  ├─────────────────┤  ├──────────────────┤
    ├──────────────────┤  │ +Customer()     │  │ +Material()      │
    │ +Product()       │  │ +getters/setters│  │ +getters/setters │
    │ +getters/setters │  │ +toString()     │  │ +toString()      │
    │ +toString()      │  └─────────────────┘  └──────────────────┘
    └──────────────────┘
```

---

## 🎯 Konsep OOP dalam Project

### 1. **ENCAPSULATION (Enkapsulasi)** ⭐

**Definisi:** Menyembunyikan data internal class dan hanya menyediakan akses melalui method public.

**Implementasi di Project:**

#### ✅ Semua Model Class (Product, Customer, Material)
```java
public class Product {
    // ❌ Private - tidak bisa diakses langsung dari luar
    private int id;
    private String nama;
    private double harga;
    private int stok;
    
    // ✅ Public - akses melalui getter/setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    // ... dst
}
```

---

### 2. **ABSTRACTION (Abstraksi)** ⭐

**Definisi:** Menyembunyikan detail implementasi kompleks dan hanya menampilkan fungsi essential.

**Implementasi di Project:**

#### ✅ Repository Layer sebagai Abstraksi
```java
// User tidak perlu tahu detail bagaimana data disimpan ke JSON
ProductRepository repo = new ProductRepository();

// ✅ Simple interface - abstraksi dari kompleksitas JSON I/O
List<Product> products = repo.ambilSemuaProduk();  
// Di balik layar: file I/O, GSON parsing, error handling, dll
```

---

### 3. **POLYMORPHISM (Polimorfisme)** ⭐

**Definisi:** Satu interface/method bisa punya banyak bentuk implementasi.

#### ✅ Method Overloading (Compile-time Polymorphism)
```java
public class Product {
    // Constructor 1 - no parameters
    public Product() { }
    
    // Constructor 2 - without ID
    public Product(String nama, double harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }
    
    // Constructor 3 - with ID
    public Product(int id, String nama, double harga, int stok) { ... }
}
```

#### ✅ Method Overriding (Runtime Polymorphism)
```java
// Semua model override toString() dari Object
public class Product {
    @Override  // Polymorphism - override method dari parent class
    public String toString() {
        return String.format("| %-5d | %-25s | $%-10.2f | %-8d |", 
                             id, nama, harga, stok);
    }
}
```

---

## 🔗 Relationship Antar Class

### 1. **DEPENDENCY (Ketergantungan)** →
```java
public class MainApp {
    public static void main(String[] args) {
        // MainApp DEPENDS ON ProductUI (temporary usage)
        ProductUI uiProduk = new ProductUI();  
        uiProduk.tampilkanMenu();
    }
}
```

### 2. **ASSOCIATION (Asosiasi)** →
```java
public class ProductUI {
    // ProductUI HAS-A ProductRepository (permanent association)
    private final ProductRepository repository;  // ← Field/member
    
    public ProductUI() {
        this.repository = new ProductRepository();  // Created once
    }
}
```

---

## 📊 Class Responsibility Summary

| Layer | Classes | Primary Responsibility | OOP Concepts Applied |
|-------|---------|------------------------|---------------------|
| **Entry** | `MainApp` | Application routing | - |
| **UI** | `ProductUI`, `CrmUI`, `ManufacturingUI` | User interaction | Encapsulation, Polymorphism |
| **Repository** | `ProductRepository`, etc. | Data access & business logic | Encapsulation, Abstraction |
| **Model** | `Product`, `Customer`, `Material` | Data structure | Encapsulation, Polymorphism (toString) |

---

## 🎯 Key Takeaways

1. **Encapsulation** → Semua fields private, akses via getters/setters
2. **Abstraction** → Repository menyembunyikan kompleksitas file I/O
3. **Polymorphism** → Constructor overloading & toString() override
4. **Dependency** → MainApp creates UI objects (weak coupling)
5. **Association** → UI has Repository objects (medium coupling)
6. **Separation of Concerns** → Model-Repository-UI (3-layer architecture)

---

**Author:** Zahran  
**Date:** 2026-01-21  
**Project:** CRM Application - OOP Analysis
