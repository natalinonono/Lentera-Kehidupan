package com.smk.selesaikanalkitabmu.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.smk.selesaikanalkitabmu.R;
import com.smk.selesaikanalkitabmu.helper.PreferenceHelper;
import com.smk.selesaikanalkitabmu.model.KitabData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PreferenceHelper prefHelper;

    private MaterialCardView cardStreak;
    private ImageView imgFire;
    private TextView tvStreakCount;
    private TextView tvTargetTitle;
    private TextView tvTargetPassage;
    private TextView tvTargetDescription;
    private View tvStatusHariIni;
    private View layoutUbahTarget;
    private TextView btnUbahTarget;
    private MaterialButton btnTandaiSelesai;
    private MaterialButton btnReset;
    private TextView tvProgressPercent;
    private TextView tvProgressCount;
    private LinearProgressIndicator progressBarTotal;
    private TextView tvHistoryContent;
    private MaterialButtonToggleGroup toggleGroupVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefHelper = new PreferenceHelper(this);

        initViews();
        setupListeners();
        setupVersionToggle();

        // 1. Cek apakah baru pertama kali membuka aplikasi -> tampilkan dialog pilihan target
        if (prefHelper.isFirstTimeSetup()) {
            showTargetSetupDialog(true);
        }

        updateUI();
    }

    private void initViews() {
        cardStreak = findViewById(R.id.cardStreak);
        imgFire = findViewById(R.id.imgFire);
        tvStreakCount = findViewById(R.id.tvStreakCount);
        tvTargetTitle = findViewById(R.id.tvTargetTitle);
        tvTargetPassage = findViewById(R.id.tvTargetPassage);
        tvTargetDescription = findViewById(R.id.tvTargetDescription);
        tvStatusHariIni = findViewById(R.id.tvStatusHariIni);
        layoutUbahTarget = findViewById(R.id.layoutUbahTarget);
        btnUbahTarget = findViewById(R.id.btnUbahTarget);
        btnTandaiSelesai = findViewById(R.id.btnTandaiSelesai);
        btnReset = findViewById(R.id.btnReset);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        tvProgressCount = findViewById(R.id.tvProgressCount);
        progressBarTotal = findViewById(R.id.progressBarTotal);
        tvHistoryContent = findViewById(R.id.tvHistoryContent);
        toggleGroupVersion = findViewById(R.id.toggleGroupVersion);
    }

    private void setupVersionToggle() {
        String currentType = prefHelper.getCanonType();
        if (KitabData.CANON_PROTESTAN.equalsIgnoreCase(currentType)) {
            toggleGroupVersion.check(R.id.btnVersionProtestan);
        } else {
            toggleGroupVersion.check(R.id.btnVersionKatolik);
        }

        toggleGroupVersion.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnVersionProtestan) {
                    prefHelper.setCanonType(KitabData.CANON_PROTESTAN);
                    Toast.makeText(this, "Beralih ke Alkitab Kristen Protestan (66 Kitab)", Toast.LENGTH_SHORT).show();
                } else {
                    prefHelper.setCanonType(KitabData.CANON_KATOLIK);
                    Toast.makeText(this, "Beralih ke Alkitab Kristen Katolik (73 Kitab)", Toast.LENGTH_SHORT).show();
                }
                updateUI();
            }
        });
    }

    private void setupListeners() {
        View.OnClickListener onUbahTargetClick = v -> showTargetSetupDialog(false);
        if (btnUbahTarget != null) {
            btnUbahTarget.setOnClickListener(onUbahTargetClick);
        }
        if (layoutUbahTarget != null) {
            layoutUbahTarget.setOnClickListener(onUbahTargetClick);
        }

        btnTandaiSelesai.setOnClickListener(v -> {
            String unit = prefHelper.getTargetUnit();
            String canonType = prefHelper.getCanonType();
            int currentPasal = prefHelper.getCurrentPasalIndex();
            int dailyCount = prefHelper.getDailyTargetCount();
            int totalPasal = KitabData.getTotalPasal(canonType);

            String rangeCompleted;

            if ("ayat".equalsIgnoreCase(unit)) {
                int startAyat = prefHelper.getCurrentAyatInPasal();
                int maxAyatDiPasal = KitabData.getEstimasiAyatPerPasal(currentPasal, canonType);
                String namaPasal = KitabData.getNamaPasalDariUrutan(currentPasal, canonType);

                int endAyat = startAyat + dailyCount - 1;
                if (endAyat >= maxAyatDiPasal) {
                    rangeCompleted = namaPasal + ":" + startAyat + "-" + maxAyatDiPasal;
                    // Berpindah ke pasal berikutnya, mulai dari ayat 1
                    prefHelper.setCurrentPasalIndex(Math.min(currentPasal + 1, totalPasal + 1));
                    prefHelper.setCurrentAyatInPasal(1);
                } else {
                    rangeCompleted = namaPasal + ":" + startAyat + "-" + endAyat;
                    // Masih di pasal yang sama, lanjutkan ke ayat berikutnya esok hari
                    prefHelper.setCurrentAyatInPasal(endAyat + 1);
                }
                prefHelper.setTotalAyatRead(prefHelper.getTotalAyatRead() + dailyCount);
            } else {
                rangeCompleted = KitabData.getRentangBacaan(currentPasal, dailyCount, canonType);
                prefHelper.setCurrentPasalIndex(currentPasal + dailyCount);
            }

            prefHelper.recordCompletion(rangeCompleted);
            Toast.makeText(MainActivity.this, "Luar biasa! Target hari ini tercapai 🔥", Toast.LENGTH_SHORT).show();
            updateUI();
        });

        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Reset Data")
                    .setMessage("Apakah kamu yakin ingin mereset progress dan streak untuk pengujian?")
                    .setPositiveButton("Ya, Reset", (dialog, which) -> {
                        prefHelper.resetAll();
                        Toast.makeText(MainActivity.this, "Data berhasil direset!", Toast.LENGTH_SHORT).show();
                        showTargetSetupDialog(true);
                        updateUI();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    /**
     * Dialog pemilihan target harian (pasal atau ayat) saat pertama kali buka atau via Ubah Target
     */
    private void showTargetSetupDialog(boolean isFirstTime) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_target_setup, null);
        RadioGroup rgUnit = dialogView.findViewById(R.id.rgUnit);
        RadioButton rbPasal = dialogView.findViewById(R.id.rbPasal);
        RadioButton rbAyat = dialogView.findViewById(R.id.rbAyat);
        ChipGroup chipGroupTarget = dialogView.findViewById(R.id.chipGroupTarget);
        Chip chip1 = dialogView.findViewById(R.id.chip1);
        Chip chip3 = dialogView.findViewById(R.id.chip3);
        Chip chip5 = dialogView.findViewById(R.id.chip5);
        EditText etTargetValue = dialogView.findViewById(R.id.etTargetValue);
        TextView tvTargetHint = dialogView.findViewById(R.id.tvTargetHint);

        String currentUnit = prefHelper.getTargetUnit();
        int currentCount = prefHelper.getDailyTargetCount();
        etTargetValue.setText(String.valueOf(currentCount));

        if ("ayat".equalsIgnoreCase(currentUnit)) {
            rbAyat.setChecked(true);
            chip1.setText("15 Ayat");
            chip3.setText("30 Ayat");
            chip5.setText("50 Ayat");
            tvTargetHint.setText("*Membaca ayat per hari cocok untuk renungan mendalam.");
        } else {
            rbPasal.setChecked(true);
            chip1.setText("1 Pasal (~3 tahun)");
            chip3.setText("3 Pasal (~1 tahun)");
            chip5.setText("5 Pasal (~8 bulan)");
            tvTargetHint.setText("*Dengan 3 pasal per hari, kamu akan menyelesaikan seluruh Alkitab dalam waktu sekitar 1 tahun.");
        }

        rgUnit.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbAyat) {
                chip1.setText("15 Ayat");
                chip3.setText("30 Ayat");
                chip5.setText("50 Ayat");
                etTargetValue.setText("15");
                tvTargetHint.setText("*Membaca ayat per hari cocok untuk renungan mendalam.");
            } else {
                chip1.setText("1 Pasal (~3 tahun)");
                chip3.setText("3 Pasal (~1 tahun)");
                chip5.setText("5 Pasal (~8 bulan)");
                etTargetValue.setText("3");
                tvTargetHint.setText("*Dengan 3 pasal per hari, kamu akan menyelesaikan seluruh Alkitab dalam waktu sekitar 1 tahun.");
            }
        });

        chip1.setOnClickListener(v -> etTargetValue.setText(rbPasal.isChecked() ? "1" : "15"));
        chip3.setOnClickListener(v -> etTargetValue.setText(rbPasal.isChecked() ? "3" : "30"));
        chip5.setOnClickListener(v -> etTargetValue.setText(rbPasal.isChecked() ? "5" : "50"));

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(!isFirstTime)
                .setPositiveButton("Simpan Target", (dialog, which) -> {
                    String unit = rbPasal.isChecked() ? "pasal" : "ayat";
                    int count = 3;
                    try {
                        count = Integer.parseInt(etTargetValue.getText().toString().trim());
                        if (count <= 0) count = 1;
                    } catch (Exception ignored) {}

                    prefHelper.setTargetUnit(unit);
                    prefHelper.setDailyTargetCount(count);
                    prefHelper.setFirstTimeSetupDone();

                    Toast.makeText(this, "Target disimpan: " + count + " " + unit + " per hari!", Toast.LENGTH_SHORT).show();
                    updateUI();
                });

        if (!isFirstTime) {
            builder.setNegativeButton("Batal", null);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Terapkan palet warna streak dinamis:
     * a. 1 - 29: Oranye
     * b. 30 - 99: Merah
     * c. 100 - 199: Ungu Muda
     * d. 200 - 299: Ungu Pekat
     * e. 300+: Biru
     */
    private void applyStreakColor(int streak) {
        int colorRes;
        int bgRes;

        if (streak <= 0) {
            colorRes = R.color.streak_gray;
            bgRes = R.color.streak_gray_bg;
        } else if (streak <= 29) {
            colorRes = R.color.streak_orange;
            bgRes = R.color.streak_orange_bg;
        } else if (streak <= 99) {
            colorRes = R.color.streak_red;
            bgRes = R.color.streak_red_bg;
        } else if (streak <= 199) {
            colorRes = R.color.streak_purple_light;
            bgRes = R.color.streak_purple_light_bg;
        } else if (streak <= 299) {
            colorRes = R.color.streak_purple_dark;
            bgRes = R.color.streak_purple_dark_bg;
        } else {
            colorRes = R.color.streak_blue;
            bgRes = R.color.streak_blue_bg;
        }

        int textColor = ContextCompat.getColor(this, colorRes);
        int bgColor = ContextCompat.getColor(this, bgRes);

        tvStreakCount.setTextColor(textColor);
        cardStreak.setCardBackgroundColor(bgColor);
        cardStreak.setStrokeColor(textColor);
        imgFire.setImageTintList(ColorStateList.valueOf(textColor));
    }

    private void updateUI() {
        // 1. Tampilkan streak api dengan warna dinamis
        int streak = prefHelper.getStreak();
        tvStreakCount.setText(String.valueOf(streak));
        applyStreakColor(streak);

        // 2. Hitung target bacaan hari ini (pasal atau ayat)
        int currentPasal = prefHelper.getCurrentPasalIndex();
        int dailyCount = prefHelper.getDailyTargetCount();
        String unit = prefHelper.getTargetUnit();
        String canonType = prefHelper.getCanonType();
        int totalPasal = KitabData.getTotalPasal(canonType);

        if ("ayat".equalsIgnoreCase(unit)) {
            int startAyat = prefHelper.getCurrentAyatInPasal();
            int maxAyat = KitabData.getEstimasiAyatPerPasal(currentPasal, canonType);
            String namaPasal = KitabData.getNamaPasalDariUrutan(currentPasal, canonType);

            int endAyat = Math.min(startAyat + dailyCount - 1, maxAyat);
            tvTargetPassage.setText(namaPasal + " : " + startAyat + " - " + endAyat);
            tvTargetDescription.setText("Target: " + dailyCount + " ayat per hari (Pasal memiliki " + maxAyat + " ayat).");
        } else {
            String passageText = KitabData.getRentangBacaan(currentPasal, dailyCount, canonType);
            tvTargetPassage.setText(passageText);
            tvTargetDescription.setText("Target: " + dailyCount + " pasal per hari (~" + (dailyCount * 26) + " ayat).");
        }

        // 3. Status tombol selesai hari ini
        boolean isReadToday = prefHelper.isReadToday();
        if (isReadToday) {
            btnTandaiSelesai.setEnabled(false);
            btnTandaiSelesai.setText(R.string.sudah_selesai_hari_ini);
            btnTandaiSelesai.setAlpha(0.6f);
            tvStatusHariIni.setVisibility(View.VISIBLE);
        } else {
            btnTandaiSelesai.setEnabled(true);
            btnTandaiSelesai.setText(R.string.tandai_selesai);
            btnTandaiSelesai.setAlpha(1.0f);
            tvStatusHariIni.setVisibility(View.GONE);
        }

        // 4. Hitung progress bar keseluruhan
        int completedPasal = Math.min(currentPasal - 1, totalPasal);
        if (completedPasal < 0) completedPasal = 0;

        int percentage = (int) (((double) completedPasal / totalPasal) * 100);
        progressBarTotal.setProgress(percentage);
        tvProgressPercent.setText(percentage + "%");
        int totalAyatKanon = KitabData.getTotalAyat(canonType);
        String versionName = KitabData.CANON_PROTESTAN.equalsIgnoreCase(canonType) ? "Kristen Protestan" : "Kristen Katolik";

        if ("ayat".equalsIgnoreCase(unit)) {
            int totalAyatRead = prefHelper.getTotalAyatRead();
            tvProgressCount.setText(totalAyatRead + " dari " + totalAyatKanon + " ayat terbaca • " + versionName);
        } else {
            tvProgressCount.setText(completedPasal + " dari " + totalPasal + " pasal (~" + totalAyatKanon + " ayat) • " + versionName);
        }

        // 5. Tampilkan riwayat terakhir
        List<String> historyList = prefHelper.getHistoryList();
        if (historyList.isEmpty()) {
            tvHistoryContent.setText("Belum ada riwayat membaca. Tekan tombol selesai di atas saat Anda telah membaca.");
        } else {
            StringBuilder sb = new StringBuilder();
            int maxShow = Math.min(historyList.size(), 5);
            for (int i = 0; i < maxShow; i++) {
                sb.append("• ").append(historyList.get(i)).append("\n");
            }
            tvHistoryContent.setText(sb.toString().trim());
        }
    }
}
