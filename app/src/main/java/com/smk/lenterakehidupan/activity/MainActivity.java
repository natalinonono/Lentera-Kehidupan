package com.smk.lenterakehidupan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.smk.lenterakehidupan.R;
import com.smk.lenterakehidupan.helper.PreferenceHelper;
import com.smk.lenterakehidupan.model.KitabData;
import com.smk.lenterakehidupan.model.PenghargaanRohani;
import com.smk.lenterakehidupan.model.ReadingHistory;
import com.smk.lenterakehidupan.model.UserProfile;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // UI Elements di Main Activity
    private TextView tvGreeting;
    private TextView tvBadgeJulukan;
    private Button btnKelolaUser;
    private TextView tvTargetBabHarian;
    private TextView tvNamaKitab;
    private TextView tvPasal;
    private TextView tvStreak;
    private TextView tvLoseStreakBadge;
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private TextView tvPersentaseSelesai;
    private TextView tvCanonBadge;
    private TextView btnGantiCanon;
    private Button btnTandaiSelesai;
    private Button btnLihatRiwayat;
    private Button btnPenghargaan;
    private Button btnReset;

    private PreferenceHelper prefHelper;
    private UserProfile currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefHelper = new PreferenceHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvBadgeJulukan = findViewById(R.id.tvBadgeJulukan);
        btnKelolaUser = findViewById(R.id.btnKelolaUser);
        tvTargetBabHarian = findViewById(R.id.tvTargetBabHarian);
        tvNamaKitab = findViewById(R.id.tvNamaKitab);
        tvPasal = findViewById(R.id.tvPasal);
        tvStreak = findViewById(R.id.tvStreak);
        tvLoseStreakBadge = findViewById(R.id.tvLoseStreakBadge);
        progressBar = findViewById(R.id.progressBar);
        tvProgressText = findViewById(R.id.tvProgressText);
        tvPersentaseSelesai = findViewById(R.id.tvPersentaseSelesai);
        tvCanonBadge = findViewById(R.id.tvCanonBadge);
        btnGantiCanon = findViewById(R.id.btnGantiCanon);
        btnTandaiSelesai = findViewById(R.id.btnTandaiSelesai);
        btnLihatRiwayat = findViewById(R.id.btnLihatRiwayat);
        btnPenghargaan = findViewById(R.id.btnPenghargaan);
        btnReset = findViewById(R.id.btnReset);
    }

    private void setupListeners() {
        // Tombol Pengelolaan Pengguna (CRUD User & Switch User)
        btnKelolaUser.setOnClickListener(v -> tampilkanDialogKelolaUser());

        // Klik Badge Julukan membuka dialog gelar rohani
        tvBadgeJulukan.setOnClickListener(v -> tampilkanDialogPenghargaan());
        btnPenghargaan.setOnClickListener(v -> tampilkanDialogPenghargaan());

        // Klik Riwayat & Statistik
        btnLihatRiwayat.setOnClickListener(v -> tampilkanDialogRiwayat());

        // Tombol Ganti Kanon
        btnGantiCanon.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCanonActivity.class);
            intent.putExtra("is_change_request", true);
            startActivity(intent);
            finish();
        });

        // Tombol Tandai Selesai
        btnTandaiSelesai.setOnClickListener(v -> selesaikanBacaan());

        // Tombol Reset Progres Pengguna Aktif
        btnReset.setOnClickListener(v -> {
            if (currentUser == null) return;
            new AlertDialog.Builder(this)
                    .setTitle("Reset Progres")
                    .setMessage("Kembalikan progres membaca " + currentUser.getNamaLengkap() + " ke Pasal 1 dan reset streak?")
                    .setPositiveButton("Ya, Reset", (dialog, which) -> {
                        prefHelper.resetProgress(currentUser);
                        Toast.makeText(this, "Progres membaca " + currentUser.getUsername() + " berhasil direset!", Toast.LENGTH_SHORT).show();
                        loadActiveUserData();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefHelper.getActiveUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        loadActiveUserData();
    }

    private void loadActiveUserData() {
        currentUser = prefHelper.getActiveUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Cek dan evaluasi lose streak
        boolean didLoseStreak = prefHelper.checkAndApplyLoseStreak(currentUser);
        if (didLoseStreak) {
            Toast.makeText(this, "Perhatian: Streak terputus karena kemarin tidak membaca firman!", Toast.LENGTH_LONG).show();
        }

        // Tampilkan info user & sapaan
        tvGreeting.setText("Hai " + currentUser.getUsername() + "!");

        // Julukan Rohani aktif
        PenghargaanRohani julukan = currentUser.getJulukanAktif();
        tvBadgeJulukan.setText(julukan.getNamaGelar());

        // Target harian
        tvTargetBabHarian.setText("Target: " + currentUser.getTargetBabPerHari() + " Bab/Hari");

        // Kanon & Total Pasal
        String canon = currentUser.getCanon();
        int totalPasal = KitabData.getTotalPasal(canon);
        progressBar.setMax(totalPasal);

        if (KitabData.CANON_PROTESTAN.equalsIgnoreCase(canon)) {
            tvCanonBadge.setText("Kanon: Protestan (66 Kitab)");
        } else {
            tvCanonBadge.setText("Kanon: Katolik (73 Kitab)");
        }

        int currentPasal = currentUser.getCurrentPasal();
        if (currentPasal > totalPasal) {
            currentPasal = totalPasal;
        }

        // Teks rentang bacaan hari ini
        String bacaanTeks = KitabData.getRentangBacaan(currentPasal, currentUser.getTargetBabPerHari(), canon);
        tvNamaKitab.setText(bacaanTeks);
        tvPasal.setText("(Pasal ke-" + currentPasal + " dari " + totalPasal + ")");

        // Streak & Lose Streak
        tvStreak.setText("Streak: " + currentUser.getStreak() + " Hari");
        tvLoseStreakBadge.setText("Lose Streak: " + currentUser.getLoseStreakCount() + "x");

        // Progress bar & Persentase
        progressBar.setProgress(currentPasal);
        tvProgressText.setText(currentPasal + " / " + totalPasal + " pasal");
        tvPersentaseSelesai.setText(String.format(java.util.Locale.US, "%.1f%% Selesai", currentUser.getPersentaseSelesai()));

        // Status selesai hari ini
        String today = PreferenceHelper.getTodayDate();
        if (today.equals(currentUser.getLastReadDate())) {
            btnTandaiSelesai.setEnabled(false);
            btnTandaiSelesai.setText("SUDAH SELESAI HARI INI");
        } else {
            btnTandaiSelesai.setEnabled(true);
            btnTandaiSelesai.setText("TANDAI SELESAI HARI INI");
        }
    }

    private void selesaikanBacaan() {
        String rentang = prefHelper.selesaikanBacaanHariIni(currentUser);
        Toast.makeText(this, "Puji Tuhan! Selesai: " + rentang, Toast.LENGTH_SHORT).show();
        loadActiveUserData();
    }

    // ==================== DIALOG CRUD & SWITCH USER ====================

    private void tampilkanDialogKelolaUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_user_manager, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        LinearLayout llUserContainer = dialogView.findViewById(R.id.llUserContainer);
        Button btnTambahUserBaru = dialogView.findViewById(R.id.btnTambahUserBaru);
        Button btnKeluarKeLogin = dialogView.findViewById(R.id.btnKeluarKeLogin);
        Button btnTutupUserManager = dialogView.findViewById(R.id.btnTutupUserManager);

        btnTutupUserManager.setOnClickListener(v -> dialog.dismiss());

        btnKeluarKeLogin.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnTambahUserBaru.setOnClickListener(v -> {
            dialog.dismiss();
            tampilkanFormUser(null); // Tambah user baru
        });

        // Muat daftar profil pengguna
        llUserContainer.removeAllViews();
        List<UserProfile> userList = prefHelper.getAllUsers();
        String activeId = prefHelper.getActiveUserId();

        for (UserProfile u : userList) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_user_profile, llUserContainer, false);

            TextView itemTvNamaLengkap = itemView.findViewById(R.id.itemTvNamaLengkap);
            TextView itemTvActiveBadge = itemView.findViewById(R.id.itemTvActiveBadge);
            TextView itemTvUsernameTTL = itemView.findViewById(R.id.itemTvUsernameTTL);
            TextView itemTvStats = itemView.findViewById(R.id.itemTvStats);
            View llSelectUserArea = itemView.findViewById(R.id.llSelectUserArea);
            TextView itemBtnEdit = itemView.findViewById(R.id.itemBtnEdit);
            TextView itemBtnHapus = itemView.findViewById(R.id.itemBtnHapus);

            itemTvNamaLengkap.setText(u.getNamaLengkap());
            itemTvUsernameTTL.setText("@" + u.getUsername() + " • " + u.getJenisKelamin() + " • TTL: " + u.getTempatTanggalLahir());
            itemTvStats.setText("Target: " + u.getTargetBabPerHari() + " Bab/Hari • Progres: Pasal " + u.getCurrentPasal() + " • Streak: " + u.getStreak() + " Hari");

            boolean isActive = u.getId().equals(activeId);
            itemTvActiveBadge.setVisibility(isActive ? View.VISIBLE : View.GONE);

            // Klik item untuk beralih / switch user
            llSelectUserArea.setOnClickListener(v -> {
                prefHelper.setActiveUserId(u.getId());
                Toast.makeText(this, "Beralih ke akun " + u.getNamaLengkap(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadActiveUserData();
            });

            // Klik Edit
            itemBtnEdit.setOnClickListener(v -> {
                dialog.dismiss();
                tampilkanFormUser(u);
            });

            // Klik Hapus
            itemBtnHapus.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Pengguna")
                        .setMessage("Apakah Anda yakin ingin menghapus data pengguna " + u.getNamaLengkap() + "? Riwayat membaca akan terhapus.")
                        .setPositiveButton("Ya, Hapus", (d, w) -> {
                            prefHelper.deleteUser(u.getId());
                            Toast.makeText(this, "Pengguna berhasil dihapus!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (prefHelper.getActiveUser() == null) {
                                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                loadActiveUserData();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            });

            llUserContainer.addView(itemView);
        }

        dialog.show();
    }

    private void tampilkanFormUser(UserProfile userToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View formView = LayoutInflater.from(this).inflate(R.layout.dialog_form_user, null);
        builder.setView(formView);

        AlertDialog dialog = builder.create();

        TextView tvFormTitle = formView.findViewById(R.id.tvFormTitle);
        EditText etFormUsername = formView.findViewById(R.id.etFormUsername);
        EditText etFormNamaLengkap = formView.findViewById(R.id.etFormNamaLengkap);
        EditText etFormTTL = formView.findViewById(R.id.etFormTTL);
        RadioGroup rgJenisKelamin = formView.findViewById(R.id.rgJenisKelamin);
        RadioButton rbLaki = formView.findViewById(R.id.rbLaki);
        RadioButton rbPerempuan = formView.findViewById(R.id.rbPerempuan);
        EditText etFormTargetBab = formView.findViewById(R.id.etFormTargetBab);
        EditText etFormKomitmen = formView.findViewById(R.id.etFormKomitmen);

        Button btnBatalForm = formView.findViewById(R.id.btnBatalForm);
        Button btnSimpanForm = formView.findViewById(R.id.btnSimpanForm);

        btnBatalForm.setOnClickListener(v -> dialog.dismiss());

        boolean isEdit = (userToEdit != null);
        if (isEdit) {
            tvFormTitle.setText("Edit Data Pengguna");
            etFormUsername.setText(userToEdit.getUsername());
            etFormNamaLengkap.setText(userToEdit.getNamaLengkap());
            etFormTTL.setText(userToEdit.getTempatTanggalLahir());
            if ("Perempuan".equalsIgnoreCase(userToEdit.getJenisKelamin())) {
                rbPerempuan.setChecked(true);
            } else {
                rbLaki.setChecked(true);
            }
            etFormTargetBab.setText(String.valueOf(userToEdit.getTargetBabPerHari()));
            etFormKomitmen.setText(userToEdit.getKomitmenRohani());
        } else {
            tvFormTitle.setText("Tambah Pengguna Baru");
            etFormTargetBab.setText("1");
        }

        btnSimpanForm.setOnClickListener(v -> {
            String username = etFormUsername.getText().toString().trim();
            String namaLengkap = etFormNamaLengkap.getText().toString().trim();
            String ttl = etFormTTL.getText().toString().trim();
            String jk = rbPerempuan.isChecked() ? "Perempuan" : "Laki-laki";
            String komitmen = etFormKomitmen.getText().toString().trim();
            String targetStr = etFormTargetBab.getText().toString().trim();

            if (username.isEmpty()) {
                etFormUsername.setError("Username wajib diisi");
                return;
            }

            int targetBab = 1;
            try {
                targetBab = Integer.parseInt(targetStr);
                if (targetBab <= 0) targetBab = 1;
            } catch (Exception ignored) {}

            if (isEdit) {
                userToEdit.setUsername(username);
                userToEdit.setNamaLengkap(namaLengkap);
                userToEdit.setTempatTanggalLahir(ttl);
                userToEdit.setJenisKelamin(jk);
                userToEdit.setKomitmenRohani(komitmen);
                userToEdit.setTargetBabPerHari(targetBab);

                prefHelper.updateActiveUser(userToEdit);
                Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            } else {
                UserProfile newUser = new UserProfile(
                        UUID.randomUUID().toString(),
                        username,
                        namaLengkap,
                        ttl,
                        jk,
                        komitmen,
                        targetBab,
                        currentUser != null ? currentUser.getCanon() : KitabData.CANON_KATOLIK
                );
                prefHelper.addUser(newUser);
                prefHelper.setActiveUserId(newUser.getId());
                Toast.makeText(this, "Pengguna baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadActiveUserData();
        });

        dialog.show();
    }

    // ==================== DIALOG RIWAYAT & STATISTIK ====================

    private void tampilkanDialogRiwayat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_history, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvHistoryUserSub = dialogView.findViewById(R.id.tvHistoryUserSub);
        TextView tvHistoryPersenBesar = dialogView.findViewById(R.id.tvHistoryPersenBesar);
        TextView tvHistoryMaxStreak = dialogView.findViewById(R.id.tvHistoryMaxStreak);
        TextView tvHistoryLoseStreak = dialogView.findViewById(R.id.tvHistoryLoseStreak);
        TextView tvStatusLoseStreakNote = dialogView.findViewById(R.id.tvStatusLoseStreakNote);
        LinearLayout llHistoryItemsContainer = dialogView.findViewById(R.id.llHistoryItemsContainer);
        Button btnTutupHistory = dialogView.findViewById(R.id.btnTutupHistory);

        btnTutupHistory.setOnClickListener(v -> dialog.dismiss());

        tvHistoryUserSub.setText("Statistik membaca firman untuk " + currentUser.getNamaLengkap());
        tvHistoryPersenBesar.setText(String.format(java.util.Locale.US, "%.1f%%", currentUser.getPersentaseSelesai()));
        tvHistoryMaxStreak.setText(currentUser.getMaxStreak() + " Hari");
        tvHistoryLoseStreak.setText(currentUser.getLoseStreakCount() + "x Putus");

        if (currentUser.getLoseStreakCount() > 0) {
            tvStatusLoseStreakNote.setText("Catatan: Terjadi " + currentUser.getLoseStreakCount() + " kali streak terputus (hari bolong). Mari tetap setia!");
        } else {
            tvStatusLoseStreakNote.setText("Status: Luar biasa! Belum pernah putus membaca sejak mulai.");
        }

        // Tampilkan daftar riwayat
        llHistoryItemsContainer.removeAllViews();
        List<ReadingHistory> list = currentUser.getHistoryList();
        if (list.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("Belum ada riwayat bacaan. Selesaikan bacaan hari ini untuk mulai mencatat riwayat!");
            emptyTv.setTextColor(getResources().getColor(R.color.text_muted));
            emptyTv.setPadding(0, 16, 0, 16);
            llHistoryItemsContainer.addView(emptyTv);
        } else {
            for (ReadingHistory h : list) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_reading_history, llHistoryItemsContainer, false);

                TextView itemHistoryTanggal = itemView.findViewById(R.id.itemHistoryTanggal);
                TextView itemHistoryPersen = itemView.findViewById(R.id.itemHistoryPersen);
                TextView itemHistoryRentang = itemView.findViewById(R.id.itemHistoryRentang);
                TextView itemHistoryStreakNote = itemView.findViewById(R.id.itemHistoryStreakNote);

                itemHistoryTanggal.setText(h.getTanggal());
                itemHistoryPersen.setText(String.format(java.util.Locale.US, "%.1f%% Perjalanan", h.getPersentaseSelesai()));
                itemHistoryRentang.setText(h.getRentangBacaan());

                String streakInfo = "Streak tercapai: " + h.getStreakSaatIni() + " Hari";
                if (h.isLoseStreakRecovery()) {
                    streakInfo += " • (Bangkit kembali dari lose streak!)";
                }
                itemHistoryStreakNote.setText(streakInfo);

                llHistoryItemsContainer.addView(itemView);
            }
        }

        dialog.show();
    }

    // ==================== DIALOG PENGHARGAAN & JULUKAN ROHANI ====================

    private void tampilkanDialogPenghargaan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_penghargaan, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvCurrentGelarTitle = dialogView.findViewById(R.id.tvCurrentGelarTitle);
        TextView tvCurrentGelarDesc = dialogView.findViewById(R.id.tvCurrentGelarDesc);
        LinearLayout llGelarListContainer = dialogView.findViewById(R.id.llGelarListContainer);
        Button btnTutupPenghargaan = dialogView.findViewById(R.id.btnTutupPenghargaan);

        btnTutupPenghargaan.setOnClickListener(v -> dialog.dismiss());

        PenghargaanRohani activeTitle = currentUser.getJulukanAktif();
        tvCurrentGelarTitle.setText(activeTitle.getNamaGelar());
        tvCurrentGelarDesc.setText(activeTitle.getDeskripsi() + " (Tercapai oleh " + currentUser.getUsername() + ")");

        // Render semua jenjang gelar
        llGelarListContainer.removeAllViews();
        List<PenghargaanRohani> semuaGelar = UserProfile.getDaftarPenghargaan();

        for (PenghargaanRohani p : semuaGelar) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_penghargaan, llGelarListContainer, false);

            TextView itemGelarNama = itemView.findViewById(R.id.itemGelarNama);
            TextView itemGelarSyarat = itemView.findViewById(R.id.itemGelarSyarat);
            TextView itemGelarStatusBadge = itemView.findViewById(R.id.itemGelarStatusBadge);

            itemGelarNama.setText(p.getNamaGelar());
            itemGelarSyarat.setText(p.getDeskripsi() + "\n(Minimal: " + p.getMinPasal() + " Pasal / Streak " + p.getMinStreak() + " Hari)");

            boolean isUnlocked = p.isUnlocked(currentUser.getCurrentPasal(), currentUser.getMaxStreak());
            if (isUnlocked) {
                itemGelarStatusBadge.setText("Terbuka");
                itemGelarStatusBadge.setBackgroundColor(getResources().getColor(R.color.green_success_bg));
                itemGelarStatusBadge.setTextColor(getResources().getColor(R.color.green_success));
            } else {
                itemGelarStatusBadge.setText("Terkunci");
                itemGelarStatusBadge.setBackgroundColor(getResources().getColor(R.color.border_light));
                itemGelarStatusBadge.setTextColor(getResources().getColor(R.color.text_muted));
            }

            llGelarListContainer.addView(itemView);
        }

        dialog.show();
    }
}
