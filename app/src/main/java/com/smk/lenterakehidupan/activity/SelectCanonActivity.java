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

        cardKatolik = findViewById(R.id.cardKatolik);
        cardProtestan = findViewById(R.id.cardProtestan);
        btnMulaiMembaca = findViewById(R.id.btnMulaiMembaca);

        // Baca preferensi tersimpan jika ada
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
            String previousCanon = prefs.getString(KEY_CANON, "");

            // Cek apakah ada perubahan versi Alkitab (misal Katolik ke Protestan atau sebaliknya)
            boolean canonChanged = !previousCanon.isEmpty() && !previousCanon.equalsIgnoreCase(selectedCanon);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_CANON, selectedCanon);
            editor.putBoolean(KEY_FIRST_TIME, true);

            // Jika versi Alkitab diganti, langsung otomatis reset progres & streak
            if (canonChanged) {
                editor.putInt("pasal", 1);
                editor.putInt("streak", 0);
                editor.putString("lastDate", "");
            }
            editor.apply();

            String label = KitabData.CANON_PROTESTAN.equalsIgnoreCase(selectedCanon)
                    ? "Alkitab Protestan (66 Kitab)"
                    : "Alkitab Katolik (73 Kitab)";

            if (canonChanged) {
                Toast.makeText(this, "Beralih ke " + label + " - Progres otomatis direset ke awal!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Memilih: " + label, Toast.LENGTH_SHORT).show();
            }

            bukaMainActivity();
        });
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
