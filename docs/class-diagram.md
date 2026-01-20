classDiagram
    %% Model Layer
    class Product {
        -int id
        -String nama
        -double harga
        -int stok
        +Product()
        +Product(String nama, double harga, int stok)
        +Product(int id, String nama, double harga, int stok)
        +getId() int
        +setId(int id) void
        +getNama() String
        +setNama(String nama) void
        +getHarga() double
        +setHarga(double harga) void
        +getStok() int
        +setStok(int stok) void
        +toString() String
    }

    class Customer {
        -int id
        -String nama
        -String telepon
        +Customer()
        +Customer(String nama, String telepon)
        +Customer(int id, String nama, String telepon)
        +getId() int
        +setId(int id) void
        +getNama() String
        +setNama(String nama) void
        +getTelepon() String
        +setTelepon(String telepon) void
        +toString() String
    }

    class Material {
        -int id
        -String nama
        -int stok
        +Material()
        +Material(String nama, int stok)
        +Material(int id, String nama, int stok)
        +getId() int
        +setId(int id) void
        +getNama() String
        +setNama(String nama) void
        +getStok() int
        +setStok(int stok) void
        +toString() String
    }

    %% Helper Database (PENTING: Ini yang kurang di diagram Anda)
    class JsonDatabase {
        -Gson gson
        +bacaSemuaData(String, Type) List~T~
        +simpanSemuaData(String, List~T~) boolean
        +siapkanFile(String) void
        +generateIdBaru(List~Integer~) int
    }

    %% Repository Layer
    class ProductRepository {
        -String FILE_PRODUK
        -Type TIPE_LIST_PRODUK
        +ambilSemuaProduk() List~Product~
        +tambahProduct(Product produkBaru) boolean
        +hapusProduct(int idProduk) boolean
        +cariById(int idProduk) Product
        +tambahStok(int idProduk, int jumlahTambah) boolean
    }

    class CustomerRepository {
        -String FILE_CUSTOMER
        -Type TIPE_LIST_CUSTOMER
        +ambilSemuaCustomer() List~Customer~
        +tambahCustomer(Customer customerBaru) boolean
        +hapusCustomer(int idCustomer) boolean
        +cariById(int idCustomer) Customer
    }

    class MaterialRepository {
        -String FILE_MATERIAL
        -Type TIPE_LIST_MATERIAL
        +ambilSemuaMaterial() List~Material~
        +tambahMaterial(Material materialBaru) boolean
        +cariById(int idMaterial) Material
        +tambahStok(int idMaterial, int jumlahTambah) boolean
        +kurangiStok(int idMaterial, int jumlahPakai) boolean
    }

    %% UI Layer
    class ProductUI {
        -ProductRepository repository
        -Scanner scanner
        +tampilkanMenu() void
        -cetakHeader() void
        -bacaPilihan() int
        -lihatSemuaProduk() void
        -tambahProdukBaru() void
        -hapusProduk() void
    }

    class CrmUI {
        -CustomerRepository repository
        -Scanner scanner
        +tampilkanMenu() void
        -cetakHeader() void
        -bacaPilihan() int
        -lihatSemuaCustomer() void
        -tambahCustomerBaru() void
        -hapusCustomer() void
    }

    class ManufacturingUI {
        -MaterialRepository repoMaterial
        -ProductRepository repoProduk
        -Scanner scanner
        +tampilkanMenu() void
        -cetakHeader() void
        -bacaPilihan() int
        -lihatStokBahan() void
        -belanjaBahan() void
        -prosesProduksi() void
        -tambahBahanBaru() void
    }

    %% Main Entry Point
    class MainApp {
        -Scanner scanner
        +main(String[] args) void
        -tampilkanSambutan() void
        -tampilkanMenuUtama() void
        -bacaPilihanUser() int
        -tampilkanPerpisahan() void
    }

    %% Relationships
    ProductRepository --> Product : manages
    CustomerRepository --> Customer : manages
    MaterialRepository --> Material : manages
    
    ProductRepository ..> JsonDatabase : uses
    CustomerRepository ..> JsonDatabase : uses
    MaterialRepository ..> JsonDatabase : uses
    
    ProductUI --> ProductRepository : uses
    CrmUI --> CustomerRepository : uses
    ManufacturingUI --> MaterialRepository : uses
    ManufacturingUI --> ProductRepository : uses
    
    MainApp --> ProductUI : creates
    MainApp --> CrmUI : creates
    MainApp --> ManufacturingUI : creates