package com.smk.lenterakehidupan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.smk.lenterakehidupan.R;
import com.smk.lenterakehidupan.model.KitabData;

public class SelectCanonActivity extends AppCompatActivity {

    public static final String PREF_NAME = "AlkitabPref";
    public static final String KEY_CANON = "canon";
    public static final String KEY_FIRST_TIME = "first_time_canon_selected";
    public static final String KEY_USER_NAME = "user_name";

    private com.google.android.material.textfield.TextInputEditText etNamaPengguna;
    private MaterialCardView cardKatolik;
    private MaterialCardView cardProtestan;
    private MaterialButton btnMulaiMembaca;

    private String selectedCanon = KitabData.CANON_KATOLIK; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Jika dipanggil bukan untuk ganti pilihan dan sudah pernah memilih sebelumnya, langsung ke MainActivity
        boolean isChangeRequest = getIntent().getBooleanExtra("is_change_request", false);
        if (!isChangeRequest && prefs.getBoolean(KEY_FIRST_TIME, false)) {
            bukaMainActivity();
            return;
        }

        setContentView(R.layout.activity_select_canon);

        etNamaPengguna = findViewById(R.id.etNamaPengguna);
        cardKatolik = findViewById(R.id.cardKatolik);
        cardProtestan = findViewById(R.id.cardProtestan);
        btnMulaiMembaca = findViewById(R.id.btnMulaiMembaca);

        // Baca preferensi tersimpan jika ada
        String savedName = prefs.getString(KEY_USER_NAME, "");
        if (!savedName.isEmpty()) {
            etNamaPengguna.setText(savedName);
        }

        selectedCanon = prefs.getString(KEY_CANON, KitabData.CANON_KATOLIK);
        updateCardSelection();

        cardKatolik.setOnClickListener(v -> {
            selectedCanon = KitabData.CANON_KATOLIK;
            updateCardSelection();
        });

        cardProtestan.setOnClickListener(v -> {
            selectedCanon = KitabData.CANON_PROTESTAN;
            updateCardSelection();
        });

        btnMulaiMembaca.setOnClickListener(v -> {
            String namaInput = etNamaPengguna.getText() != null ? etNamaPengguna.getText().toString().trim() : "";
            if (namaInput.isEmpty()) {
                namaInput = "Pembaca";
            }

            String previousCanon = prefs.getString(KEY_CANON, "");
            boolean canonChanged = !previousCanon.isEmpty() && !previousCanon.equalsIgnoreCase(selectedCanon);

            final String finalNama = namaInput;
            if (canonChanged) {
                String namaVersiBaru = KitabData.CANON_PROTESTAN.equalsIgnoreCase(selectedCanon)
                        ? "Alkitab Protestan (66 Kitab)"
                        : "Alkitab Katolik (73 Kitab)";

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Ganti Versi")
                        .setMessage("Apakah Anda yakin ingin mengganti ke " + namaVersiBaru + "?\n\nPerhatian: Mengganti versi Alkitab akan mengulang progres membaca Anda dari awal (Pasal 1 & Streak 0).")
                        .setPositiveButton("Ya, Ganti & Reset", (dialog, which) -> {
                            simpanDanLanjutkan(prefs, true, finalNama);
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            } else {
                simpanDanLanjutkan(prefs, false, finalNama);
            }
        });
    }

    private void simpanDanLanjutkan(SharedPreferences prefs, boolean isReset, String namaPengguna) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CANON, selectedCanon);
        editor.putString(KEY_USER_NAME, namaPengguna);
        editor.putBoolean(KEY_FIRST_TIME, true);

        if (isReset) {
            editor.putInt("pasal", 1);
            editor.putInt("streak", 0);
            editor.putString("lastDate", "");
        }
        editor.apply();

        String label = KitabData.CANON_PROTESTAN.equalsIgnoreCase(selectedCanon)
                ? "Alkitab Protestan (66 Kitab)"
                : "Alkitab Katolik (73 Kitab)";

        if (isReset) {
            Toast.makeText(this, "Beralih ke " + label + " - Progres diulang dari awal.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Versi aktif: " + label, Toast.LENGTH_SHORT).show();
        }

        bukaMainActivity();
    }

    private void updateCardSelection() {
        int colorPrimary = ContextCompat.getColor(this, R.color.primary_gray);
        int colorBorder = ContextCompat.getColor(this, R.color.border_light);

        if (KitabData.CANON_KATOLIK.equalsIgnoreCase(selectedCanon)) {
            cardKatolik.setStrokeColor(colorPrimary);
            cardKatolik.setStrokeWidth(dpToPx(2));

            cardProtestan.setStrokeColor(colorBorder);
            cardProtestan.setStrokeWidth(dpToPx(1));
        } else {
            cardProtestan.setStrokeColor(colorPrimary);
            cardProtestan.setStrokeWidth(dpToPx(2));

            cardKatolik.setStrokeColor(colorBorder);
            cardKatolik.setStrokeWidth(dpToPx(1));
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void bukaMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
