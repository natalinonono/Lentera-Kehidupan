package com.smk.lenterakehidupan.model;

import java.io.Serializable;

public class ReadingHistory implements Serializable {
    private String tanggal;
    private String rentangBacaan;
    private int pasalDibaca;
    private double persentaseSelesai;
    private int streakSaatIni;
    private boolean isLoseStreakRecovery; // menandai apakah ada streak putus sebelumnya

    public ReadingHistory() {}

    public ReadingHistory(String tanggal, String rentangBacaan, int pasalDibaca, double persentaseSelesai, int streakSaatIni, boolean isLoseStreakRecovery) {
        this.tanggal = tanggal;
        this.rentangBacaan = rentangBacaan;
        this.pasalDibaca = pasalDibaca;
        this.persentaseSelesai = persentaseSelesai;
        this.streakSaatIni = streakSaatIni;
        this.isLoseStreakRecovery = isLoseStreakRecovery;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getRentangBacaan() {
        return rentangBacaan;
    }

    public void setRentangBacaan(String rentangBacaan) {
        this.rentangBacaan = rentangBacaan;
    }

    public int getPasalDibaca() {
        return pasalDibaca;
    }

    public void setPasalDibaca(int pasalDibaca) {
        this.pasalDibaca = pasalDibaca;
    }

    public double getPersentaseSelesai() {
        return persentaseSelesai;
    }

    public void setPersentaseSelesai(double persentaseSelesai) {
        this.persentaseSelesai = persentaseSelesai;
    }

    public int getStreakSaatIni() {
        return streakSaatIni;
    }

    public void setStreakSaatIni(int streakSaatIni) {
        this.streakSaatIni = streakSaatIni;
    }

    public boolean isLoseStreakRecovery() {
        return isLoseStreakRecovery;
    }

    public void setLoseStreakRecovery(boolean loseStreakRecovery) {
        isLoseStreakRecovery = loseStreakRecovery;
    }
}
