package com.crm.project.model;

/**
 * Entity class representing a Customer.
 * Fields: id, name, phone.
 */
public class Customer {

    private int id;
    private String name;
    private String phone;

    // Default constructor
    public Customer() {
    }

    // Parameterized constructor (without id, for new customers)
    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Full parameterized constructor (with id, for existing customers)
    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | %-15s |", id, name, phone);
    }
}
