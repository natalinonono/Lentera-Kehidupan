package com.smk.lenterakehidupan.activity;

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

import com.smk.lenterakehidupan.R;
import com.smk.lenterakehidupan.model.KitabData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Komponen UI
    private TextView tvGreeting;
    private TextView btnEditNama;
    private TextView tvNamaKitab;
    private TextView tvPasal;
    private TextView tvStreak;
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private TextView tvCanonBadge;
    private TextView btnGantiCanon;
    private Button btnTandaiSelesai;
    private Button btnReset;

    // Penyimpanan data lokal (SharedPreferences sederhana)
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "AlkitabPref";
    private static final String KEY_PASAL = "pasal";
    private static final String KEY_STREAK = "streak";
    private static final String KEY_LAST_DATE = "lastDate";
    private static final String KEY_CANON = "canon"; // "katolik" atau "protestan"
    private static final String KEY_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // 2. Hubungkan variabel dengan ID di layout activity_main.xml
        tvGreeting = findViewById(R.id.tvGreeting);
        btnEditNama = findViewById(R.id.btnEditNama);
        tvNamaKitab = findViewById(R.id.tvNamaKitab);
        tvPasal = findViewById(R.id.tvPasal);
        tvStreak = findViewById(R.id.tvStreak);
        progressBar = findViewById(R.id.progressBar);
        tvProgressText = findViewById(R.id.tvProgressText);
        tvCanonBadge = findViewById(R.id.tvCanonBadge);
        btnGantiCanon = findViewById(R.id.btnGantiCanon);
        btnTandaiSelesai = findViewById(R.id.btnTandaiSelesai);
        btnReset = findViewById(R.id.btnReset);

        // Tombol Ubah Nama Pengguna
        btnEditNama.setOnClickListener(v -> tampilkanDialogUbahNama());

        // 3. Tombol ganti Kanon (membuka kembali SelectCanonActivity)
        btnGantiCanon.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, SelectCanonActivity.class);
            intent.putExtra("is_change_request", true);
            startActivity(intent);
            finish();
        });

        // 4. Muat dan tampilkan data saat ini
        loadData();

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

    private void tampilkanDialogUbahNama() {
        android.widget.EditText inputNama = new android.widget.EditText(this);
        String currentName = sharedPreferences.getString(KEY_USER_NAME, "Josua");
        inputNama.setText(currentName);
        inputNama.setSingleLine(true);
        inputNama.setSelection(inputNama.getText().length());

        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout container = new android.widget.FrameLayout(this);
        container.setPadding(paddingPx, paddingPx / 2, paddingPx, 0);
        container.addView(inputNama);

        new AlertDialog.Builder(this)
                .setTitle("Ubah Nama Anda")
                .setMessage("Masukkan nama atau panggilan Anda:")
                .setView(container)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String namaBaru = inputNama.getText().toString().trim();
                    if (namaBaru.isEmpty()) {
                        namaBaru = "Pembaca";
                    }
                    sharedPreferences.edit().putString(KEY_USER_NAME, namaBaru).apply();
                    Toast.makeText(this, "Nama berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void loadData() {
        // Ambil versi yang aktif
        String canon = sharedPreferences.getString(KEY_CANON, KitabData.CANON_KATOLIK);
        int totalPasal = KitabData.getTotalPasal(canon); // Katolik: 1334, Protestan: 1189

        // Set batas maksimal progress bar sesuai versi alkitab yang dipilih
        progressBar.setMax(totalPasal);

        // Update teks badge kanon
        if (KitabData.CANON_PROTESTAN.equalsIgnoreCase(canon)) {
            tvCanonBadge.setText("Kanon: Protestan (66 Kitab)");
        } else {
            tvCanonBadge.setText("Kanon: Katolik (73 Kitab)");
        }

        // Ambil data lokal
        String userName = sharedPreferences.getString(KEY_USER_NAME, "Josua");
        if (userName.trim().isEmpty()) {
            userName = "Pembaca";
        }
        tvGreeting.setText("Hai " + userName + "!");

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
