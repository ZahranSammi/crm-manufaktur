# Class Diagram & OOP Concepts - CRM Application

## 📊 Class Diagram Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         MAIN (Entry Point)                      │
│  - scanner: Scanner                                             │
│  + main(String[]): void                                         │
└────────────────┬─────────────┬──────────────────────────────────┘
                 │             │
       «creates» │             │ «creates»
                 │             │
    ┌────────────▼─────┐  ┌───▼──────────┐  ┌────────────────────┐
    │    ProductUI     │  │    CrmUI     │  │  ManufacturingUI   │
    ├──────────────────┤  ├──────────────┤  ├────────────────────┤
    │ -productRepo     │  │ -customerRepo│  │ -materialRepo      │
    │ -scanner         │  │ -scanner     │  │ -productRepo       │
    ├──────────────────┤  ├──────────────┤  │ -scanner           │
    │ +showMenu()      │  │ +showMenu()  │  ├────────────────────┤
    └────────┬─────────┘  └──────┬───────┘  │ +showMenu()        │
             │                   │           │ +produceProduct()  │
             │ «uses»            │ «uses»    └─────┬──────┬───────┘
             │                   │                 │      │
             │                   │        «uses»   │      │ «uses»
             │                   │                 │      │
    ┌────────▼─────────┐  ┌──────▼──────────┐  ┌──▼──────▼─────┐
    │ ProductRepository│  │CustomerRepository│  │MaterialRepository│
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ -DATA_FILE       │  │ -DATA_FILE      │  │ -DATA_FILE       │
    │ -gson: Gson      │  │ -gson: Gson     │  │ -gson: Gson      │
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ +getAllProducts()│  │ +getAllCustomers│  │ +getAllMaterials()│
    │ +addProduct()    │  │ +addCustomer()  │  │ +addMaterial()   │
    │ +deleteProduct() │  │ +deleteCustomer()│  │ +reduceStock()   │
    │ +addStock()      │  │ +findById()     │  │ +addStock()      │
    └────────┬─────────┘  └────────┬────────┘  └──────┬───────────┘
             │                     │                   │
             │ «manages»           │ «manages»         │ «manages»
             │                     │                   │
    ┌────────▼─────────┐  ┌────────▼────────┐  ┌──────▼───────────┐
    │     Product      │  │    Customer     │  │     Material     │
    ├──────────────────┤  ├─────────────────┤  ├──────────────────┤
    │ -id: int         │  │ -id: int        │  │ -id: int         │
    │ -name: String    │  │ -name: String   │  │ -name: String    │
    │ -price: double   │  │ -phone: String  │  │ -stock: int      │
    │ -stock: int      │  ├─────────────────┤  ├──────────────────┤
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
    private String name;
    private double price;
    private int stock;
    
    // ✅ Public - akses melalui getter/setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // ... dst
}
```

**Keuntungan:**
- ✅ Validasi bisa ditambahkan di setter (misalnya: `if (price < 0) throw exception`)
- ✅ Internal representation bisa berubah tanpa mempengaruhi class lain
- ✅ Data integrity terjaga

**Contoh Pelanggaran (TIDAK DILAKUKAN di project ini):**
```java
// ❌ BAD - field public
public class ProductBad {
    public int id;  // Bisa diakses langsung: product.id = -1 (invalid!)
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
List<Product> products = repo.getAllProducts();  
// Di balik layar: file I/O, GSON parsing, error handling, dll

repo.addProduct(newProduct);
// Di balik layar: auto-generate ID, read existing, append, save JSON
```

**Detail yang DI-ABSTRAKSI:**
- File path management
- JSON serialization/deserialization dengan GSON
- File creation & initialization
- ID auto-generation logic
- Error handling untuk IOException

**Tanpa Abstraksi (jika UI langsung akses file):**
```java
// ❌ BAD - UI harus tahu detail file handling
File file = new File("data/products.json");
FileReader reader = new FileReader(file);
Gson gson = new Gson();
List<Product> products = gson.fromJson(reader, new TypeToken<List<Product>>(){}.getType());
// ... kompleksitas tinggi di UI layer!
```

---

### 3. **INHERITANCE (Pewarisan)** ⭐

**⚠️ TIDAK DIGUNAKAN** dalam project ini karena struktur yang sederhana.

**Jika ingin menggunakan Inheritance, contoh refactoring:**

```java
// Base class untuk semua entity
public abstract class BaseEntity {
    protected int id;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public abstract String toString();
}

// Product extends BaseEntity
public class Product extends BaseEntity {
    private String name;
    private double price;
    private int stock;
    
    // Tidak perlu getter/setter untuk 'id' lagi (sudah di parent)
    
    @Override
    public String toString() {
        return "Product: " + name;
    }
}

// Customer extends BaseEntity
public class Customer extends BaseEntity {
    private String name;
    private String phone;
    
    // Inherits getId(), setId() dari BaseEntity
    
    @Override
    public String toString() {
        return "Customer: " + name;
    }
}
```

**Mengapa TIDAK dipakai di project ini:**
- ✅ **KISS Principle** (Keep It Simple, Stupid) - Model sangat sederhana
- ✅ Tidak ada shared behavior yang kompleks
- ✅ Menghindari over-engineering

---

### 4. **POLYMORPHISM (Polimorfisme)** ⭐

**Definisi:** Satu interface/method bisa punya banyak bentuk implementasi.

**Implementasi di Project:**

#### ✅ Method Overloading (Compile-time Polymorphism)
```java
public class Product {
    // Constructor 1 - no parameters
    public Product() { }
    
    // Constructor 2 - without ID
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    // Constructor 3 - with ID
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

// USAGE:
Product p1 = new Product();  // Polymorphism - method yang sama, parameter beda
Product p2 = new Product("Laptop", 12000000, 10);
Product p3 = new Product(1, "Phone", 5000000, 20);
```

#### ✅ Method Overriding (Runtime Polymorphism)
```java
// Semua model override toString() dari Object
public class Product {
    @Override  // Polymorphism - override method dari parent class
    public String toString() {
        return String.format("| %-5d | %-25s | $%-10.2f | %-8d |", 
                             id, name, price, stock);
    }
}

public class Customer {
    @Override  // Same method name, different implementation
    public String toString() {
        return String.format("| %-5d | %-25s | %-15s |", 
                             id, name, phone);
    }
}

// USAGE:
Product product = new Product(1, "Laptop", 12000000, 10);
System.out.println(product);  // Calls Product.toString()

Customer customer = new Customer(1, "John", "08123456");
System.out.println(customer);  // Calls Customer.toString()
```

---

## 🔗 Relationship Antar Class

### 1. **DEPENDENCY (Ketergantungan)** →

**Karakteristik:** Class A "menggunakan" Class B, tapi tidak menyimpannya sebagai field.

**Contoh:**
```java
public class Main {
    public static void main(String[] args) {
        // Main DEPENDS ON ProductUI (temporary usage)
        ProductUI productUI = new ProductUI();  
        productUI.showMenu();
        // productUI bisa di-discard setelah method selesai
    }
}
```

**Relationship:**
- `Main` → `ProductUI` (dependency)
- `Main` → `CrmUI` (dependency)
- `Main` → `ManufacturingUI` (dependency)

**Karakteristik:** Weak coupling (hubungan lemah), temporary.

---

### 2. **ASSOCIATION (Asosiasi)** →

**Karakteristik:** Class A "memiliki" Class B sebagai field/member variable.

**Contoh:**
```java
public class ProductUI {
    // ProductUI HAS-A ProductRepository (permanent association)
    private final ProductRepository productRepository;  // ← Field/member
    private final Scanner scanner;
    
    public ProductUI() {
        this.productRepository = new ProductRepository();  // Created once
    }
    
    public void showMenu() {
        // Uses productRepository repeatedly
        List<Product> products = productRepository.getAllProducts();
    }
}
```

**Relationship:**
- `ProductUI` → `ProductRepository` (association)
- `CrmUI` → `CustomerRepository` (association)
- `ManufacturingUI` → `MaterialRepository` (association)
- `ManufacturingUI` → `ProductRepository` (association)

**Karakteristik:** Stronger coupling than dependency, permanent.

---

### 3. **AGGREGATION (Agregasi)** ◇→

**Karakteristik:** "Has-A" relationship yang tidak strict - object bisa exist independently.

**Contoh di Project:**
```java
public class ProductRepository {
    private Gson gson;  // Aggregation - Gson bisa exist tanpa ProductRepository
    
    public ProductRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
}
```

**Karakteristik:** Repository menggunakan Gson, tapi Gson tidak "dimiliki" exclusively.

---

### 4. **COMPOSITION (Komposisi - Strong)** ◆→

**Karakteristik:** "Part-Of" relationship - jika parent hancur, child juga hancur.

**⚠️ TIDAK MURNI** di project ini karena menggunakan file storage.

Jika pure in-memory:
```java
public class ProductRepository {
    // Composition - products list is PART OF repository
    private List<Product> products = new ArrayList<>();  
    // Jika ProductRepository di-destroy, List juga hilang
}
```

Tapi di project sebenarnya menggunakan **file persistence**, jadi lebih ke **external storage association**.

---

## 🏛️ Class Hierarchy & Main Classes

### **CLASS UTAMA (Core Classes):**

#### 1️⃣ **Main.java** - Application Controller
```
Role: Entry Point & Orchestrator
Responsibility: Routing user ke modul yang dipilih
Importance: ⭐⭐⭐⭐⭐ (Tanpa ini aplikasi tidak bisa jalan)
```

#### 2️⃣ **Repository Classes** - Data Access Layer
```
ProductRepository, CustomerRepository, MaterialRepository
Role: Data Management & Business Logic
Importance: ⭐⭐⭐⭐⭐ (Core of the application)
```

#### 3️⃣ **UI Classes** - Presentation Layer
```
ProductUI, CrmUI, ManufacturingUI
Role: User Interaction
Importance: ⭐⭐⭐⭐ (Essential untuk interaksi)
```

### **CLASS PENDUKUNG (Supporting Classes):**

#### 4️⃣ **Model Classes** - Data Containers
```
Product, Customer, Material
Role: Data Structure (POJO - Plain Old Java Object)
Importance: ⭐⭐⭐ (Bisa diganti dengan Map/DTO tapi kurang clean)
```

---

## 📋 Detailed Relationship Matrix

| From Class | To Class | Relationship Type | UML Notation | Strength |
|------------|----------|-------------------|--------------|----------|
| `Main` | `ProductUI` | Dependency (creates) | `----->` | Weak |
| `Main` | `CrmUI` | Dependency (creates) | `----->` | Weak |
| `Main` | `ManufacturingUI` | Dependency (creates) | `----->` | Weak |
| `ProductUI` | `ProductRepository` | Association (has-a) | `──────>` | Medium |
| `ProductUI` | `Scanner` | Association (has-a) | `──────>` | Medium |
| `CrmUI` | `CustomerRepository` | Association (has-a) | `──────>` | Medium |
| `ManufacturingUI` | `MaterialRepository` | Association (has-a) | `──────>` | Medium |
| `ManufacturingUI` | `ProductRepository` | Association (has-a) | `──────>` | Medium |
| `ProductRepository` | `Product` | Manages/Uses | `...-->` | Weak |
| `CustomerRepository` | `Customer` | Manages/Uses | `...-->` | Weak |
| `MaterialRepository` | `Material` | Manages/Uses | `...-->` | Weak |
| All Repositories | `Gson` | Aggregation | `◇────>` | Medium |

**Legend:**
- `----->` Dependency (dashed line with arrow)
- `──────>` Association (solid line with arrow)
- `◇────>` Aggregation (hollow diamond)
- `◆────>` Composition (filled diamond)
- `...-->` Usage/Manages (dotted line)

---

## 🔍 Class Communication Flow

### Scenario: User Menambah Product Baru

```
1. USER input di console
   ↓
2. Main.main() 
   ↓ creates
3. ProductUI.showMenu()
   ↓ user pilih "Add Product"
4. ProductUI.addNewProduct()
   ↓ reads user input (Scanner)
5. new Product(name, price, stock)  ← Instantiate model
   ↓
6. ProductUI.productRepository.addProduct(product)
   ↓
7. ProductRepository.addProduct()
   ├─ getAllProducts()  (read JSON)
   ├─ getNextId()       (generate new ID)
   ├─ product.setId(id) (set ID ke model)
   ├─ products.add(product) (tambah ke list)
   └─ saveProducts()    (write JSON)
      ↓
8. GSON serialization → Write to products.json
   ↓
9. Return true/false ke ProductUI
   ↓
10. ProductUI tampilkan success/error message
```

**Classes Involved:**
1. `Main` (orchestration)
2. `ProductUI` (interaction)
3. `Scanner` (input)
4. `Product` (data model)
5. `ProductRepository` (data access)
6. `Gson` (serialization)
7. File System (storage)

---

## 🎨 Design Pattern yang Digunakan

### 1. **Repository Pattern** 📚
```
Separation of data access logic dari business logic

UI Layer ← → Repository Layer ← → Data Storage (JSON)
```

### 2. **Singleton Pattern (Implicit)** 🔐
```java
public class ProductUI {
    private final ProductRepository productRepository;  // One instance per UI
    private final Scanner scanner;  // Shared scanner
}
```

### 3. **Factory Method (dalam Repository)** 🏭
```java
private int getNextId() {
    // Factory method untuk generate ID
    return products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
}
```

---

## ⚖️ Class Coupling Analysis

### **Low Coupling (Good!)** ✅
- Model classes (Product, Customer, Material) tidak depend ke class lain
- Repository classes hanya depend ke Model dan Gson
- UI classes hanya depend ke Repository

### **Medium Coupling** ⚠️
- ManufacturingUI depends on 2 repositories (MaterialRepo + ProductRepo)
  - **Alasan:** Untuk produksi, perlu update kedua data

### **Tight Coupling yang Dihindari** ❌
- UI tidak langsung akses file (ada Repository sebagai abstraction)
- Main tidak tahu detail internal UI
- Repository tidak tahu siapa yang menggunakannya

---

## 📊 Class Responsibility Summary

| Layer | Classes | Primary Responsibility | OOP Concepts Applied |
|-------|---------|------------------------|---------------------|
| **Entry** | `Main` | Application routing | - |
| **UI** | `ProductUI`, `CrmUI`, `ManufacturingUI` | User interaction | Encapsulation, Polymorphism |
| **Repository** | `ProductRepository`, etc. | Data access & business logic | Encapsulation, Abstraction |
| **Model** | `Product`, `Customer`, `Material` | Data structure | Encapsulation, Polymorphism (toString) |

---

## 🎯 Key Takeaways

1. **Encapsulation** → Semua fields private, akses via getters/setters
2. **Abstraction** → Repository menyembunyikan kompleksitas file I/O
3. **Polymorphism** → Constructor overloading & toString() override
4. **Dependency** → Main creates UI objects (weak coupling)
5. **Association** → UI has Repository objects (medium coupling)
6. **Separation of Concerns** → Model-Repository-UI (3-layer architecture)

**Class Paling Penting:**
1. **Main** - Entry point
2. **Repository Classes** - Core business logic
3. **UI Classes** - User interface

---

**Author:** Zahran  
**Date:** 2026-01-21  
**Project:** CRM Application - OOP Analysis
