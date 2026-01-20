package com.crm.project.repository;

import com.crm.project.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Product CRUD operations.
 * Uses JSON file for data persistence instead of database.
 */
public class ProductRepository {

    private static final String DATA_FILE = "data/products.json";
    private final Gson gson;

    public ProductRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeDataFile();
    }

    /**
     * Initializes the data file and directory if they don't exist.
     */
    private void initializeDataFile() {
        File file = new File(DATA_FILE);
        File parentDir = file.getParentFile();

        try {
            // Create directory if it doesn't exist
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Create file with empty array if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
                saveProducts(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Error initializing data file: " + e.getMessage());
        }
    }

    /**
     * Reads all products from the JSON file.
     *
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            List<Product> products = gson.fromJson(reader, listType);
            return products != null ? products : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all products to the JSON file.
     *
     * @param products List of products to save
     */
    private void saveProducts(List<Product> products) {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(products, writer);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    /**
     * Generates the next available ID.
     *
     * @return Next available ID
     */
    private int getNextId() {
        List<Product> products = getAllProducts();
        return products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Adds a new product to the JSON file.
     *
     * @param product The product to add (id will be auto-generated)
     * @return true if the product was added successfully, false otherwise
     */
    public boolean addProduct(Product product) {
        try {
            List<Product> products = getAllProducts();

            // Auto-generate ID
            product.setId(getNextId());
            products.add(product);

            saveProducts(products);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a product from the JSON file by its ID.
     *
     * @param id The ID of the product to delete
     * @return true if the product was deleted successfully, false otherwise
     */
    public boolean deleteProduct(int id) {
        try {
            List<Product> products = getAllProducts();

            boolean removed = products.removeIf(p -> p.getId() == id);

            if (removed) {
                saveProducts(products);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to find
     * @return The product if found, null otherwise
     */
    public Product findById(int id) {
        return getAllProducts().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing product.
     *
     * @param product The product with updated data
     * @return true if the product was updated successfully, false otherwise
     */
    public boolean updateProduct(Product product) {
        try {
            List<Product> products = getAllProducts();

            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == product.getId()) {
                    products.set(i, product);
                    saveProducts(products);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds stock to an existing product.
     *
     * @param id     The product ID
     * @param amount The amount to add to stock
     * @return true if successful, false otherwise
     */
    public boolean addStock(int id, int amount) {
        try {
            List<Product> products = getAllProducts();

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                if (p.getId() == id) {
                    p.setStock(p.getStock() + amount);
                    products.set(i, p);
                    saveProducts(products);
                    return true;
                }
            }
            return false; // Product not found
        } catch (Exception e) {
            System.err.println("Error adding stock: " + e.getMessage());
            return false;
        }
    }
}
