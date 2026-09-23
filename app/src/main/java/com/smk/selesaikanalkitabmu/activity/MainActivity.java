package com.smk.selesaikanalkitabmu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.smk.selesaikanalkitabmu.R;
import com.smk.selesaikanalkitabmu.model.KitabData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Komponen UI
    private TextView tvNamaKitab;
    private TextView tvPasal;
    private TextView tvStreak;
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private Button btnTandaiSelesai;
    private Button btnReset;
    private RadioGroup rgCanon;
    private RadioButton rbKatolik;
    private RadioButton rbProtestan;

    // Penyimpanan data lokal (SharedPreferences sederhana)
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "AlkitabPref";
    private static final String KEY_PASAL = "pasal";
    private static final String KEY_STREAK = "streak";
    private static final String KEY_LAST_DATE = "lastDate";
    private static final String KEY_CANON = "canon"; // "katolik" atau "protestan"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // 2. Hubungkan variabel dengan ID di layout activity_main.xml
        tvNamaKitab = findViewById(R.id.tvNamaKitab);
        tvPasal = findViewById(R.id.tvPasal);
        tvStreak = findViewById(R.id.tvStreak);
        progressBar = findViewById(R.id.progressBar);
        tvProgressText = findViewById(R.id.tvProgressText);
        btnTandaiSelesai = findViewById(R.id.btnTandaiSelesai);
        btnReset = findViewById(R.id.btnReset);
        rgCanon = findViewById(R.id.rgCanon);
        rbKatolik = findViewById(R.id.rbKatolik);
        rbProtestan = findViewById(R.id.rbProtestan);

        // 3. Set radio button sesuai preferensi tersimpan
        String canon = sharedPreferences.getString(KEY_CANON, KitabData.CANON_KATOLIK);
        if (KitabData.CANON_PROTESTAN.equalsIgnoreCase(canon)) {
            rbProtestan.setChecked(true);
        } else {
            rbKatolik.setChecked(true);
        }

        // 4. Muat dan tampilkan data saat ini
        loadData();

        // 5. Listener perubahan pilihan versi Alkitab
        rgCanon.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedCanon = (checkedId == R.id.rbProtestan) ? KitabData.CANON_PROTESTAN : KitabData.CANON_KATOLIK;
            sharedPreferences.edit().putString(KEY_CANON, selectedCanon).apply();
            String label = (checkedId == R.id.rbProtestan) ? "Alkitab Protestan (66 Kitab, 1.189 Pasal)" : "Alkitab Katolik (73 Kitab, 1.334 Pasal)";
            Toast.makeText(this, "Beralih ke " + label, Toast.LENGTH_SHORT).show();
            loadData();
        });

        // 6. Tombol "TANDAI SELESAI HARI INI"
        btnTandaiSelesai.setOnClickListener(v -> {
            selesaikanBacaanHariIni();
        });

        // 7. Tombol "RESET PROGRES"
        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset Progres")
                    .setMessage("Kembalikan progres membaca ke Pasal 1 dan reset streak?")
                    .setPositiveButton("Ya, Reset", (dialog, which) -> {
                        sharedPreferences.edit()
                                .putInt(KEY_PASAL, 1)
                                .putInt(KEY_STREAK, 0)
                                .putString(KEY_LAST_DATE, "")
                                .apply();
                        Toast.makeText(this, "Progres berhasil direset ke Pasal 1!", Toast.LENGTH_SHORT).show();
                        loadData();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    private void loadData() {
        // Ambil versi yang aktif
        String canon = sharedPreferences.getString(KEY_CANON, KitabData.CANON_KATOLIK);
        int totalPasal = KitabData.getTotalPasal(canon); // Katolik: 1334, Protestan: 1189

        // Set batas maksimal progress bar sesuai versi alkitab yang dipilih
        progressBar.setMax(totalPasal);

        // Ambil data lokal
        int pasal = sharedPreferences.getInt(KEY_PASAL, 1);
        int streak = sharedPreferences.getInt(KEY_STREAK, 0);

        if (pasal > totalPasal) {
            pasal = totalPasal;
        }

        // Dapatkan nama Kitab dan nomor pasalnya secara dinamis
        String namaKitabPasal = KitabData.getNamaPasalDariUrutan(pasal, canon);

        // Tampilkan ke layar
        tvNamaKitab.setText(namaKitabPasal);
        tvPasal.setText("(Pasal ke-" + pasal + " dari " + totalPasal + ")");
        tvStreak.setText("Streak: " + streak + " Hari");
        progressBar.setProgress(pasal);
        tvProgressText.setText(pasal + " / " + totalPasal + " pasal");

        // Cek tanggal agar tidak berulang kali di hari yang sama
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = sharedPreferences.getString(KEY_LAST_DATE, "");

        if (today.equals(lastDate)) {
            btnTandaiSelesai.setEnabled(false);
            btnTandaiSelesai.setText("SUDAH SELESAI HARI INI");
        } else {
            btnTandaiSelesai.setEnabled(true);
            btnTandaiSelesai.setText("TANDAI SELESAI HARI INI");
        }
    }

    private void selesaikanBacaanHariIni() {
        String canon = sharedPreferences.getString(KEY_CANON, KitabData.CANON_KATOLIK);
        int totalPasal = KitabData.getTotalPasal(canon);

        int pasal = sharedPreferences.getInt(KEY_PASAL, 1);
        int streak = sharedPreferences.getInt(KEY_STREAK, 0);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Update data: pasal +1 (tidak melebihi total pasal), streak +1
        if (pasal < totalPasal) {
            pasal = pasal + 1;
        }
        streak = streak + 1;

        // Simpan ke SharedPreferences
        sharedPreferences.edit()
                .putInt(KEY_PASAL, pasal)
                .putInt(KEY_STREAK, streak)
                .putString(KEY_LAST_DATE, today)
                .apply();

        Toast.makeText(this, "Hebat! Target hari ini selesai!", Toast.LENGTH_SHORT).show();
        loadData();
    }
}
