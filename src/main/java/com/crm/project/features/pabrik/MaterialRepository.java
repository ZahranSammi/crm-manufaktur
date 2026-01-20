package com.crm.project.features.pabrik;

import com.crm.project.database.JsonDatabase;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialRepository {

    private static final String FILE_MATERIAL = "data/materials.json";
    private static final Type TIPE_LIST_MATERIAL = new TypeToken<ArrayList<Material>>() {
    }.getType();

    public MaterialRepository() {
        JsonDatabase.siapkanFile(FILE_MATERIAL);
    }

    public List<Material> ambilSemuaMaterial() {
        List<Material> hasil = JsonDatabase.bacaSemuaData(FILE_MATERIAL, TIPE_LIST_MATERIAL);
        return hasil != null ? hasil : new ArrayList<>();
    }

    public boolean tambahMaterial(Material materialBaru) {
        List<Material> semuaMaterial = ambilSemuaMaterial();

        List<Integer> semuaId = semuaMaterial.stream()
                .map(Material::getId)
                .collect(Collectors.toList());
        int idBaru = JsonDatabase.generateIdBaru(semuaId);

        materialBaru.setId(idBaru);
        semuaMaterial.add(materialBaru);

        return JsonDatabase.simpanSemuaData(FILE_MATERIAL, semuaMaterial);
    }

    public Material cariDenganId(int idMaterial) {
        return ambilSemuaMaterial().stream()
                .filter(material -> material.getId() == idMaterial)
                .findFirst()
                .orElse(null);
    }

    public boolean hapusMaterial(int idMaterial) {
        List<Material> semuaMaterial = ambilSemuaMaterial();

        boolean berhasilHapus = semuaMaterial.removeIf(
                material -> material.getId() == idMaterial);

        if (berhasilHapus) {
            return JsonDatabase.simpanSemuaData(FILE_MATERIAL, semuaMaterial);
        }
        return false;
    }

    public boolean tambahStok(int idMaterial, int jumlahTambah) {
        List<Material> semuaMaterial = ambilSemuaMaterial();

        for (int i = 0; i < semuaMaterial.size(); i++) {
            Material material = semuaMaterial.get(i);

            if (material.getId() == idMaterial) {
                int stokBaru = material.getStok() + jumlahTambah;
                material.setStok(stokBaru);
                semuaMaterial.set(i, material);
                return JsonDatabase.simpanSemuaData(FILE_MATERIAL, semuaMaterial);
            }
        }
        return false;
    }

    public boolean kurangiStok(int idMaterial, int jumlahPakai) {
        List<Material> semuaMaterial = ambilSemuaMaterial();

        for (int i = 0; i < semuaMaterial.size(); i++) {
            Material material = semuaMaterial.get(i);

            if (material.getId() == idMaterial) {
                if (material.getStok() >= jumlahPakai) {
                    int stokBaru = material.getStok() - jumlahPakai;
                    material.setStok(stokBaru);
                    semuaMaterial.set(i, material);
                    return JsonDatabase.simpanSemuaData(FILE_MATERIAL, semuaMaterial);
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
