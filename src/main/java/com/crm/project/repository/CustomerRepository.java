package com.crm.project.repository;

import com.crm.project.model.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Customer CRUD operations.
 * Uses JSON file for data persistence.
 */
public class CustomerRepository {

    private static final String DATA_FILE = "data/customers.json";
    private final Gson gson;

    public CustomerRepository() {
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
                saveCustomers(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Error initializing data file: " + e.getMessage());
        }
    }

    public List<Customer> getAllCustomers() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Type listType = new TypeToken<ArrayList<Customer>>() {
            }.getType();
            List<Customer> customers = gson.fromJson(reader, listType);
            return customers != null ? customers : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveCustomers(List<Customer> customers) {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(customers, writer);
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    private int getNextId() {
        List<Customer> customers = getAllCustomers();
        return customers.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;
    }

    public boolean addCustomer(Customer customer) {
        try {
            List<Customer> customers = getAllCustomers();
            customer.setId(getNextId());
            customers.add(customer);
            saveCustomers(customers);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCustomer(int id) {
        try {
            List<Customer> customers = getAllCustomers();
            boolean removed = customers.removeIf(c -> c.getId() == id);
            if (removed) {
                saveCustomers(customers);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    public Customer findById(int id) {
        return getAllCustomers().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
