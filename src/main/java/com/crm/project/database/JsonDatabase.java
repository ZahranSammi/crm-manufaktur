package com.crm.project.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDatabase {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static <T> List<T> bacaSemuaData(String pathFile, Type tipeData) {
        try (Reader pembacaFile = new FileReader(pathFile)) {
            List<T> hasilBaca = gson.fromJson(pembacaFile, tipeData);
            return hasilBaca != null ? hasilBaca : new ArrayList<>();

        } catch (IOException kesalahan) {
            System.err.println("❌ Gagal membaca file: " + kesalahan.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> boolean simpanSemuaData(String pathFile, List<T> data) {
        try (Writer penulisFile = new FileWriter(pathFile)) {
            gson.toJson(data, penulisFile);
            return true;

        } catch (IOException kesalahan) {
            System.err.println("❌ Gagal menyimpan file: " + kesalahan.getMessage());
            return false;
        }
    }

    public static void siapkanFile(String pathFile) {
        File file = new File(pathFile);
        File folderInduk = file.getParentFile();

        try {
            if (folderInduk != null && !folderInduk.exists()) {
                boolean berhasilBuatFolder = folderInduk.mkdirs();
                if (berhasilBuatFolder) {
                    System.out.println("📁 Folder data berhasil dibuat.");
                }
            }

            if (!file.exists()) {
                boolean berhasilBuatFile = file.createNewFile();
                if (berhasilBuatFile) {
                    simpanSemuaData(pathFile, new ArrayList<>());
                    System.out.println("📄 File " + file.getName() + " berhasil dibuat.");
                }
            }

        } catch (IOException kesalahan) {
            System.err.println("❌ Gagal menyiapkan file: " + kesalahan.getMessage());
        }
    }

    public static int generateIdBaru(List<Integer> daftarId) {
        return daftarId.stream()
                .max(Integer::compareTo)
                .orElse(0)
                + 1;
    }
}
