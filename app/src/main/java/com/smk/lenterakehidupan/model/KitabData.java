package com.smk.lenterakehidupan.model;

import java.util.ArrayList;
import java.util.List;

public class KitabData {

    public static final String CANON_KATOLIK = "katolik";
    public static final String CANON_PROTESTAN = "protestan";

    public static List<Kitab> getDaftarKitab(String canonType) {
        boolean isKatolik = CANON_KATOLIK.equalsIgnoreCase(canonType);
        List<Kitab> list = new ArrayList<>();

        // 1. Perjanjian Lama (Protokanonika - 39 Kitab)
        list.add(new Kitab("Kejadian", 50, 1533, "Perjanjian Lama"));
        list.add(new Kitab("Keluaran", 40, 1213, "Perjanjian Lama"));
        list.add(new Kitab("Imamat", 27, 859, "Perjanjian Lama"));
        list.add(new Kitab("Bilangan", 36, 1288, "Perjanjian Lama"));
        list.add(new Kitab("Ulangan", 34, 959, "Perjanjian Lama"));
        list.add(new Kitab("Yosua", 24, 658, "Perjanjian Lama"));
        list.add(new Kitab("Hakim-hakim", 21, 618, "Perjanjian Lama"));
        list.add(new Kitab("Rut", 4, 85, "Perjanjian Lama"));
        list.add(new Kitab("1 Samuel", 31, 810, "Perjanjian Lama"));
        list.add(new Kitab("2 Samuel", 24, 695, "Perjanjian Lama"));
        list.add(new Kitab("1 Raja-raja", 22, 816, "Perjanjian Lama"));
        list.add(new Kitab("2 Raja-raja", 25, 719, "Perjanjian Lama"));
        list.add(new Kitab("1 Tawarikh", 29, 942, "Perjanjian Lama"));
        list.add(new Kitab("2 Tawarikh", 36, 822, "Perjanjian Lama"));
        list.add(new Kitab("Ezra", 10, 280, "Perjanjian Lama"));
        list.add(new Kitab("Nehemia", 13, 406, "Perjanjian Lama"));

        // 2. Deuterokanonika (Jika Katolik)
        if (isKatolik) {
            list.add(new Kitab("Tobit", 14, 244, "Deuterokanonika"));
            list.add(new Kitab("Yudit", 16, 340, "Deuterokanonika"));
            list.add(new Kitab("Ester", 16, 260, "Perjanjian Lama (Katolik)"));
            list.add(new Kitab("1 Makabe", 16, 924, "Deuterokanonika"));
            list.add(new Kitab("2 Makabe", 15, 556, "Deuterokanonika"));
        } else {
            list.add(new Kitab("Ester", 10, 167, "Perjanjian Lama"));
        }

        list.add(new Kitab("Ayub", 42, 1070, "Perjanjian Lama"));
        list.add(new Kitab("Mazmur", 150, 2527, "Perjanjian Lama"));
        list.add(new Kitab("Amsal", 31, 915, "Perjanjian Lama"));
        list.add(new Kitab("Pengkhotbah", 12, 222, "Perjanjian Lama"));
        list.add(new Kitab("Kidung Agung", 8, 117, "Perjanjian Lama"));

        if (isKatolik) {
            list.add(new Kitab("Kebijaksanaan Salomo", 19, 436, "Deuterokanonika"));
            list.add(new Kitab("Sirakh", 51, 1405, "Deuterokanonika"));
        }

        list.add(new Kitab("Yesaya", 66, 1292, "Perjanjian Lama"));
        list.add(new Kitab("Yeremia", 52, 1364, "Perjanjian Lama"));
        list.add(new Kitab("Ratapan", 5, 154, "Perjanjian Lama"));

        if (isKatolik) {
            list.add(new Kitab("Barukh", 6, 137, "Deuterokanonika"));
        }

        list.add(new Kitab("Yehezkiel", 48, 1273, "Perjanjian Lama"));

        if (isKatolik) {
            list.add(new Kitab("Daniel", 14, 530, "Perjanjian Lama (Katolik)"));
        } else {
            list.add(new Kitab("Daniel", 12, 357, "Perjanjian Lama"));
        }

        list.add(new Kitab("Hosea", 14, 197, "Perjanjian Lama"));
        list.add(new Kitab("Yoel", 3, 73, "Perjanjian Lama"));
        list.add(new Kitab("Amos", 9, 146, "Perjanjian Lama"));
        list.add(new Kitab("Obaja", 1, 21, "Perjanjian Lama"));
        list.add(new Kitab("Yunus", 4, 48, "Perjanjian Lama"));
        list.add(new Kitab("Mikha", 7, 105, "Perjanjian Lama"));
        list.add(new Kitab("Nahum", 3, 47, "Perjanjian Lama"));
        list.add(new Kitab("Habakuk", 3, 56, "Perjanjian Lama"));
        list.add(new Kitab("Zefanya", 3, 53, "Perjanjian Lama"));
        list.add(new Kitab("Hagai", 2, 38, "Perjanjian Lama"));
        list.add(new Kitab("Zakharia", 14, 211, "Perjanjian Lama"));
        list.add(new Kitab("Maleakhi", 4, 55, "Perjanjian Lama"));

        // 3. Perjanjian Baru (27 Kitab)
        list.add(new Kitab("Matius", 28, 1071, "Perjanjian Baru"));
        list.add(new Kitab("Markus", 16, 678, "Perjanjian Baru"));
        list.add(new Kitab("Lukas", 24, 1151, "Perjanjian Baru"));
        list.add(new Kitab("Yohanes", 21, 879, "Perjanjian Baru"));
        list.add(new Kitab("Kisah Para Rasul", 28, 1007, "Perjanjian Baru"));
        list.add(new Kitab("Roma", 16, 433, "Perjanjian Baru"));
        list.add(new Kitab("1 Korintus", 16, 437, "Perjanjian Baru"));
        list.add(new Kitab("2 Korintus", 13, 257, "Perjanjian Baru"));
        list.add(new Kitab("Galatia", 6, 149, "Perjanjian Baru"));
        list.add(new Kitab("Efesus", 6, 155, "Perjanjian Baru"));
        list.add(new Kitab("Filipi", 4, 104, "Perjanjian Baru"));
        list.add(new Kitab("Kolose", 4, 95, "Perjanjian Baru"));
        list.add(new Kitab("1 Tesalonika", 5, 89, "Perjanjian Baru"));
        list.add(new Kitab("2 Tesalonika", 3, 47, "Perjanjian Baru"));
        list.add(new Kitab("1 Timotius", 6, 113, "Perjanjian Baru"));
        list.add(new Kitab("2 Timotius", 4, 83, "Perjanjian Baru"));
        list.add(new Kitab("Titus", 3, 46, "Perjanjian Baru"));
        list.add(new Kitab("Filemon", 1, 25, "Perjanjian Baru"));
        list.add(new Kitab("Ibrani", 13, 303, "Perjanjian Baru"));
        list.add(new Kitab("Yakobus", 5, 108, "Perjanjian Baru"));
        list.add(new Kitab("1 Petrus", 5, 105, "Perjanjian Baru"));
        list.add(new Kitab("2 Petrus", 3, 61, "Perjanjian Baru"));
        list.add(new Kitab("1 Yohanes", 5, 105, "Perjanjian Baru"));
        list.add(new Kitab("2 Yohanes", 1, 13, "Perjanjian Baru"));
        list.add(new Kitab("3 Yohanes", 1, 15, "Perjanjian Baru"));
        list.add(new Kitab("Yudas", 1, 25, "Perjanjian Baru"));
        list.add(new Kitab("Wahyu", 22, 404, "Perjanjian Baru"));

        return list;
    }

    public static List<Kitab> getDaftarKitab() {
        return getDaftarKitab(CANON_KATOLIK);
    }

    public static int getTotalPasal(String canonType) {
        int total = 0;
        for (Kitab k : getDaftarKitab(canonType)) {
            total += k.getJumlahBab();
        }
        return total;
    }

    public static int getTotalPasal() {
        return getTotalPasal(CANON_KATOLIK);
    }

    public static int getTotalAyat(String canonType) {
        int total = 0;
        for (Kitab k : getDaftarKitab(canonType)) {
            total += k.getTotalAyat();
        }
        return total;
    }

    public static Kitab getKitabByUrutanPasal(int urutanPasalGlobal, String canonType) {
        List<Kitab> daftar = getDaftarKitab(canonType);
        int hitungan = 0;
        for (Kitab k : daftar) {
            if (hitungan + k.getJumlahBab() >= urutanPasalGlobal) {
                return k;
            }
            hitungan += k.getJumlahBab();
        }
        return null;
    }

    public static String getNamaPasalDariUrutan(int urutanPasalGlobal, String canonType) {
        List<Kitab> daftar = getDaftarKitab(canonType);
        int totalPasal = getTotalPasal(canonType);

        if (urutanPasalGlobal <= 0) urutanPasalGlobal = 1;
        if (urutanPasalGlobal > totalPasal) {
            return "Alkitab Selesai Terbaca! 🎉";
        }

        int hitungan = 0;
        for (Kitab k : daftar) {
            if (hitungan + k.getJumlahBab() >= urutanPasalGlobal) {
                int nomorBabDalamKitab = urutanPasalGlobal - hitungan;
                return k.getNama() + " " + nomorBabDalamKitab;
            }
            hitungan += k.getJumlahBab();
        }
        return "Selesai";
    }

    /**
     * Estimasi jumlah ayat dalam suatu pasal tertentu
     */
    public static int getEstimasiAyatPerPasal(int urutanPasalGlobal, String canonType) {
        Kitab k = getKitabByUrutanPasal(urutanPasalGlobal, canonType);
        if (k == null) return 26;

        // Rata-rata ayat di kitab tersebut (dibulatkan, minimal 10)
        int avg = (int) Math.round((double) k.getTotalAyat() / (double) k.getJumlahBab());
        // Khusus Kejadian 1 memiliki 31 ayat, Kejadian 2 memiliki 25 ayat
        if ("Kejadian".equalsIgnoreCase(k.getNama())) {
            int noBab = getNomorBabDalamKitab(urutanPasalGlobal, canonType);
            if (noBab == 1) return 31;
            if (noBab == 2) return 25;
        }
        return Math.max(10, avg);
    }

    public static int getNomorBabDalamKitab(int urutanPasalGlobal, String canonType) {
        List<Kitab> daftar = getDaftarKitab(canonType);
        int hitungan = 0;
        for (Kitab k : daftar) {
            if (hitungan + k.getJumlahBab() >= urutanPasalGlobal) {
                return urutanPasalGlobal - hitungan;
            }
            hitungan += k.getJumlahBab();
        }
        return 1;
    }

    public static String getRentangBacaan(int startPasal, int jumlahPasalTarget, String canonType) {
        int totalPasal = getTotalPasal(canonType);
        if (startPasal > totalPasal) {
            return "Selamat! Semua bacaan Alkitab telah selesai dibaca.";
        }
        int endPasal = Math.min(startPasal + jumlahPasalTarget - 1, totalPasal);
        String dari = getNamaPasalDariUrutan(startPasal, canonType);
        String sampai = getNamaPasalDariUrutan(endPasal, canonType);

        if (dari.equals(sampai)) {
            return dari;
        }
        return dari + "  s/d  " + sampai;
    }

    public static String getRentangBacaan(int startPasal, int jumlahPasalTarget) {
        return getRentangBacaan(startPasal, jumlahPasalTarget, CANON_KATOLIK);
    }
}
