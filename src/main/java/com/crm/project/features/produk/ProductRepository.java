package com.crm.project.features.produk;

import com.crm.project.database.JsonDatabase;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {

    private static final String FILE_PRODUK = "data/products.json";
    private static final Type TIPE_LIST_PRODUK = new TypeToken<ArrayList<Product>>() {
    }.getType();

    public ProductRepository() {
        JsonDatabase.siapkanFile(FILE_PRODUK);
    }

    public List<Product> ambilSemuaProduk() {
        List<Product> hasil = JsonDatabase.bacaSemuaData(FILE_PRODUK, TIPE_LIST_PRODUK);
        return hasil != null ? hasil : new ArrayList<>();
    }

    public boolean tambahProduk(Product produkBaru) {
        List<Product> semuaProduk = ambilSemuaProduk();

        List<Integer> semuaId = semuaProduk.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        int idBaru = JsonDatabase.generateIdBaru(semuaId);

        produkBaru.setId(idBaru);
        semuaProduk.add(produkBaru);

        return JsonDatabase.simpanSemuaData(FILE_PRODUK, semuaProduk);
    }

    public boolean hapusProduk(int idProduk) {
        List<Product> semuaProduk = ambilSemuaProduk();

        boolean berhasilHapus = semuaProduk.removeIf(
                produk -> produk.getId() == idProduk);

        if (berhasilHapus) {
            return JsonDatabase.simpanSemuaData(FILE_PRODUK, semuaProduk);
        }
        return false;
    }

    public Product cariDenganId(int idProduk) {
        return ambilSemuaProduk().stream()
                .filter(produk -> produk.getId() == idProduk)
                .findFirst()
                .orElse(null);
    }

    public boolean updateProduk(Product produkUpdate) {
        List<Product> semuaProduk = ambilSemuaProduk();

        for (int i = 0; i < semuaProduk.size(); i++) {
            Product produk = semuaProduk.get(i);

            if (produk.getId() == produkUpdate.getId()) {
                semuaProduk.set(i, produkUpdate);
                return JsonDatabase.simpanSemuaData(FILE_PRODUK, semuaProduk);
            }
        }

        return false;
    }

    public boolean tambahStok(int idProduk, int jumlahTambah) {
        List<Product> semuaProduk = ambilSemuaProduk();

        for (int i = 0; i < semuaProduk.size(); i++) {
            Product produk = semuaProduk.get(i);

            if (produk.getId() == idProduk) {
                int stokBaru = produk.getStok() + jumlahTambah;
                produk.setStok(stokBaru);
                semuaProduk.set(i, produk);

                return JsonDatabase.simpanSemuaData(FILE_PRODUK, semuaProduk);
            }
        }

        return false;
    }
}
