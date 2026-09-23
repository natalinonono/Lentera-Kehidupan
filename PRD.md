# Project: Selesaikan Alkitabmu! 📖🔥
Aplikasi Pelacak Bacaan Alkitab Harian Siswa SMK RPL (Mata Pelajaran Pengembangan Perangkat Bergerak / PPB).

## Fitur Utama
1. **Daftar Lengkap 73 Kitab Alkitab**: Dikelola secara mandiri di `KitabData.java` tanpa perlu membebani memori HP dengan teks Alkitab utuh.
2. **Kalkulasi Urutan Pasal Dinamis**: Mengonversi urutan pasal global secara otomatis ke nama Kitab dan nomor Bab.
3. **Streak Bacaan Harian**: Menghitung konsistensi membaca harian berturut-turut dengan SharedPreferences.
4. **Target Otomatis Bergerak**: Saat tombol "Tandai Selesai Hari Ini" ditekan, target bacaan hari esok otomatis bergeser maju.
5. **Indikator Progres Persentase**: Menampilkan progres linear dari 0 s/d 100% dari total 1.334 pasal Alkitab.
6. **Riwayat Aktivitas**: Menyimpan riwayat tanggal & rentang bacaan yang telah diselesaikan menggunakan JSON / Gson.

## Struktur Berkas Proyek
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle` (Dependensi: Google Material 1.11.0, Gson 2.10.1, ConstraintLayout 2.1.4)
- `app/src/main/java/com/smk/selesaikanalkitabmu/`
  - `model/Kitab.java`
  - `model/KitabData.java`
  - `helper/PreferenceHelper.java`
  - `activity/MainActivity.java`
- `app/src/main/res/`
  - `layout/activity_main.xml`
  - `values/colors.xml`
  - `values/strings.xml`
  - `values/themes.xml`
  - `drawable/ic_fire.xml`
  - `drawable/ic_book.xml`

## Cara Membuka di Android Studio
1. Buka **Android Studio**.
2. Pilih menu **File -> Open...** (atau di Welcome screen pilih **Open**).
3. Arahkan ke folder proyek ini: `d:\. Pelajaran\KK\PPB\APK Alkitab`.
4. Tunggu beberapa saat hingga proses **Gradle Sync** selesai mengunduh pustaka.
5. Hubungkan HP Android fisik via kabel USB (aktifkan USB Debugging) atau jalankan Emulator.
6. Klik tombol segitiga hijau **Run 'app'** (`Shift + F10`).
