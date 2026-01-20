package com.crm.project.features.crm;

public class Customer {

    private int id;
    private String nama;
    private String telepon;

    public Customer() {
    }

    public Customer(String nama, String telepon) {
        this.nama = nama;
        this.telepon = telepon;
    }

    public Customer(int id, String nama, String telepon) {
        this.id = id;
        this.nama = nama;
        this.telepon = telepon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | %-15s |", id, nama, telepon);
    }
}
