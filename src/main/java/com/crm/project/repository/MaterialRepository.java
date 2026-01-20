package com.crm.project.repository;

import com.crm.project.model.Material;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Material CRUD operations.
 * Uses JSON file for data persistence.
 */
public class MaterialRepository {

    private static final String DATA_FILE = "data/materials.json";
    private final Gson gson;

    public MaterialRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeDataFile();
    }

    private void initializeDataFile() {
        File file = new File(DATA_FILE);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                saveMaterials(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Error initializing data file: " + e.getMessage());
        }
    }

    public List<Material> getAllMaterials() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Type listType = new TypeToken<ArrayList<Material>>() {
            }.getType();
            List<Material> materials = gson.fromJson(reader, listType);
            return materials != null ? materials : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading materials: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveMaterials(List<Material> materials) {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(materials, writer);
        } catch (IOException e) {
            System.err.println("Error saving materials: " + e.getMessage());
        }
    }

    private int getNextId() {
        List<Material> materials = getAllMaterials();
        return materials.stream()
                .mapToInt(Material::getId)
                .max()
                .orElse(0) + 1;
    }

    public boolean addMaterial(Material material) {
        try {
            List<Material> materials = getAllMaterials();
            material.setId(getNextId());
            materials.add(material);
            saveMaterials(materials);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding material: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMaterial(int id) {
        try {
            List<Material> materials = getAllMaterials();
            boolean removed = materials.removeIf(m -> m.getId() == id);
            if (removed) {
                saveMaterials(materials);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting material: " + e.getMessage());
            return false;
        }
    }

    public Material findById(int id) {
        return getAllMaterials().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Reduces the stock of a material by the specified amount.
     * 
     * @param id     The material ID
     * @param amount The amount to reduce
     * @return true if stock was sufficient and reduced, false otherwise
     */
    public boolean reduceStock(int id, int amount) {
        try {
            List<Material> materials = getAllMaterials();

            for (int i = 0; i < materials.size(); i++) {
                Material m = materials.get(i);
                if (m.getId() == id) {
                    if (m.getStock() >= amount) {
                        m.setStock(m.getStock() - amount);
                        materials.set(i, m);
                        saveMaterials(materials);
                        return true;
                    } else {
                        return false; // Insufficient stock
                    }
                }
            }
            return false; // Material not found
        } catch (Exception e) {
            System.err.println("Error reducing stock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds stock to a material.
     * 
     * @param id     The material ID
     * @param amount The amount to add
     * @return true if successful, false otherwise
     */
    public boolean addStock(int id, int amount) {
        try {
            List<Material> materials = getAllMaterials();

            for (int i = 0; i < materials.size(); i++) {
                Material m = materials.get(i);
                if (m.getId() == id) {
                    m.setStock(m.getStock() + amount);
                    materials.set(i, m);
                    saveMaterials(materials);
                    return true;
                }
            }
            return false; // Material not found
        } catch (Exception e) {
            System.err.println("Error adding stock: " + e.getMessage());
            return false;
        }
    }
}
