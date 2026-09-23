package com.smk.selesaikanalkitabmu.model;

public class Kitab {
    private final String nama;
    private final int jumlahBab;
    private final int totalAyat;
    private final String perjanjian; // "Perjanjian Lama", "Perjanjian Baru", atau "Deuterokanonika"

    public Kitab(String nama, int jumlahBab, int totalAyat, String perjanjian) {
        this.nama = nama;
        this.jumlahBab = jumlahBab;
        this.totalAyat = totalAyat;
        this.perjanjian = perjanjian;
    }

    public String getNama() {
        return nama;
    }

    public int getJumlahBab() {
        return jumlahBab;
    }

    public int getTotalAyat() {
        return totalAyat;
    }

    public String getPerjanjian() {
        return perjanjian;
    }
}
