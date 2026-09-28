package com.smk.lenterakehidupan.model;

public class PenghargaanRohani {
    private final String namaGelar;
    private final String deskripsi;
    private final int minPasal;
    private final int minStreak;
    private final String iconSimbol;

    public PenghargaanRohani(String namaGelar, String deskripsi, int minPasal, int minStreak, String iconSimbol) {
        this.namaGelar = namaGelar;
        this.deskripsi = deskripsi;
        this.minPasal = minPasal;
        this.minStreak = minStreak;
        this.iconSimbol = iconSimbol;
    }

    public String getNamaGelar() {
        return namaGelar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getMinPasal() {
        return minPasal;
    }

    public int getMinStreak() {
        return minStreak;
    }

    public String getIconSimbol() {
        return iconSimbol;
    }

    public boolean isUnlocked(int currentPasal, int maxStreak) {
        return currentPasal >= minPasal || maxStreak >= minStreak;
    }
}
