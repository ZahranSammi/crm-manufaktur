package com.crm.project.features.pabrik;

public class Material {

    private int id;
    private String nama;
    private int stok;

    public Material() {
    }

    public Material(String nama, int stok) {
        this.nama = nama;
        this.stok = stok;
    }

    public Material(int id, String nama, int stok) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
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

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | %-10d |", id, nama, stok);
    }
}
