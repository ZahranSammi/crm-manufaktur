package com.crm.project.model;

/**
 * Entity class representing a Material/Raw Material.
 * Fields: id, name, stock.
 */
public class Material {

    private int id;
    private String name;
    private int stock;

    // Default constructor
    public Material() {
    }

    // Parameterized constructor (without id, for new materials)
    public Material(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    // Full parameterized constructor (with id, for existing materials)
    public Material(int id, String name, int stock) {
        this.id = id;
        this.name = name;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | %-10d |", id, name, stock);
    }
}
