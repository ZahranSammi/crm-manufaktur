package com.crm.project.model;

/**
 * Entity class representing a Product in the database.
 * Maps to the 'products' table with columns: id, name, price, stock.
 */
public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;

    // Default constructor
    public Product() {
    }

    // Parameterized constructor (without id, for new products)
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Full parameterized constructor (with id, for existing products)
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | $%-10.2f | %-8d |", id, name, price, stock);
    }
}
