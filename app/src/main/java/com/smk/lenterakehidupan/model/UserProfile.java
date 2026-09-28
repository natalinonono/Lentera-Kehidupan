package com.smk.lenterakehidupan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfile implements Serializable {
    private String id;
    private String username;
    private String namaLengkap;
    private String tempatTanggalLahir;
    private String jenisKelamin; // "Laki-laki" / "Perempuan"
    private String komitmenRohani; // misal "Membaca tiap subuh", dll
    private int targetBabPerHari; // Pilihan bab per hari (1, 2, 3, dst.)

    // Progres perjalanan Alkitab
    private String canon; // "katolik" atau "protestan"
    private int currentPasal; // Pasal saat ini (mulai dari 1)
    private int streak;
    private int maxStreak;
    private int loseStreakCount; // Berapa kali putus streak
    private String lastReadDate; // "yyyy-MM-dd"

    private List<ReadingHistory> historyList;

    public UserProfile() {
        this.historyList = new ArrayList<>();
    }

    public UserProfile(String id, String username, String namaLengkap, String tempatTanggalLahir, 
                       String jenisKelamin, String komitmenRohani, int targetBabPerHari, String canon) {
        this.id = id;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.tempatTanggalLahir = tempatTanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.komitmenRohani = komitmenRohani;
        this.targetBabPerHari = targetBabPerHari > 0 ? targetBabPerHari : 1;
        this.canon = canon;
        this.currentPasal = 1;
        this.streak = 0;
        this.maxStreak = 0;
        this.loseStreakCount = 0;
        this.lastReadDate = "";
        this.historyList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNamaLengkap() {
        return (namaLengkap != null && !namaLengkap.trim().isEmpty()) ? namaLengkap : username;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getTempatTanggalLahir() {
        return tempatTanggalLahir != null ? tempatTanggalLahir : "-";
    }

    public void setTempatTanggalLahir(String tempatTanggalLahir) {
        this.tempatTanggalLahir = tempatTanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin != null ? jenisKelamin : "Laki-laki";
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getKomitmenRohani() {
        return komitmenRohani != null ? komitmenRohani : "Setia membaca Firman";
    }

    public void setKomitmenRohani(String komitmenRohani) {
        this.komitmenRohani = komitmenRohani;
    }

    public int getTargetBabPerHari() {
        return targetBabPerHari > 0 ? targetBabPerHari : 1;
    }

    public void setTargetBabPerHari(int targetBabPerHari) {
        this.targetBabPerHari = targetBabPerHari > 0 ? targetBabPerHari : 1;
    }

    public String getCanon() {
        return (canon != null && !canon.isEmpty()) ? canon : KitabData.CANON_KATOLIK;
    }

    public void setCanon(String canon) {
        this.canon = canon;
    }

    public int getCurrentPasal() {
        return Math.max(1, currentPasal);
    }

    public void setCurrentPasal(int currentPasal) {
        this.currentPasal = currentPasal;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
        if (streak > this.maxStreak) {
            this.maxStreak = streak;
        }
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public int getLoseStreakCount() {
        return loseStreakCount;
    }

    public void setLoseStreakCount(int loseStreakCount) {
        this.loseStreakCount = loseStreakCount;
    }

    public void incrementLoseStreak() {
        this.loseStreakCount++;
    }

    public String getLastReadDate() {
        return lastReadDate != null ? lastReadDate : "";
    }

    public void setLastReadDate(String lastReadDate) {
        this.lastReadDate = lastReadDate;
    }

    public List<ReadingHistory> getHistoryList() {
        if (historyList == null) {
            historyList = new ArrayList<>();
        }
        return historyList;
    }

    public void setHistoryList(List<ReadingHistory> historyList) {
        this.historyList = historyList;
    }

    public double getPersentaseSelesai() {
        int total = KitabData.getTotalPasal(getCanon());
        if (total <= 0) return 0.0;
        // Pasal yang telah diselesaikan adalah (currentPasal - 1) jika sebelum selesai hari ini,
        // namun untuk pembacaan saat ini dihitung proporsi pasal tercapai:
        double persen = ((double) Math.min(currentPasal, total) / (double) total) * 100.0;
        return Math.round(persen * 10.0) / 10.0;
    }

    public PenghargaanRohani getJulukanAktif() {
        List<PenghargaanRohani> list = getDaftarPenghargaan();
        PenghargaanRohani tertinggi = list.get(0);
        for (PenghargaanRohani p : list) {
            if (p.isUnlocked(this.currentPasal, this.maxStreak)) {
                tertinggi = p;
            }
        }
        return tertinggi;
    }

    public static List<PenghargaanRohani> getDaftarPenghargaan() {
        List<PenghargaanRohani> list = new ArrayList<>();
        list.add(new PenghargaanRohani("Pencari Sabda", "Memulai langkah awal membaca firman Tuhan", 1, 0, ""));
        list.add(new PenghargaanRohani("Murid yang Tekun", "Mencapai 10 pasal atau streak 3 hari berturut-turut", 10, 3, ""));
        list.add(new PenghargaanRohani("Musafir Padang Gurun", "Menyelesaikan 50 pasal (Kitab Kejadian) atau streak 7 hari", 50, 7, ""));
        list.add(new PenghargaanRohani("Prajurit Doa", "Menyelesaikan 150 pasal atau streak 14 hari", 150, 14, ""));
        list.add(new PenghargaanRohani("Laskar Firman", "Mencapai 300 pasal atau streak 30 hari (1 Bulan)", 300, 30, ""));
        list.add(new PenghargaanRohani("Penjaga Perjanjian", "Membaca lebih dari 600 pasal dengan setia", 600, 45, ""));
        list.add(new PenghargaanRohani("Saksi Kebenaran", "Membaca lebih dari 1000 pasal Alkitab", 1000, 60, ""));
        list.add(new PenghargaanRohani("Pemenang Iman", "Menuntaskan seluruh kitab dan pasal dalam Alkitab!", 1189, 90, ""));
        return list;
    }
}
