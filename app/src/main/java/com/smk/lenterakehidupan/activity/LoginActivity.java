package com.smk.lenterakehidupan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.smk.lenterakehidupan.R;
import com.smk.lenterakehidupan.helper.PreferenceHelper;
import com.smk.lenterakehidupan.model.KitabData;
import com.smk.lenterakehidupan.model.UserProfile;

import java.util.List;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout llLoginUserContainer;
    private TextView tvEmptyUserWarning;
    private MaterialButton btnLoginDaftarBaru;
    private PreferenceHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefHelper = new PreferenceHelper(this);

        llLoginUserContainer = findViewById(R.id.llLoginUserContainer);
        tvEmptyUserWarning = findViewById(R.id.tvEmptyUserWarning);
        btnLoginDaftarBaru = findViewById(R.id.btnLoginDaftarBaru);

        btnLoginDaftarBaru.setOnClickListener(v -> tampilkanFormUser(null));

        muatDaftarPengguna();
    }

    @Override
    protected void onResume() {
        super.onResume();
        muatDaftarPengguna();
    }

    private void muatDaftarPengguna() {
        llLoginUserContainer.removeAllViews();
        List<UserProfile> userList = prefHelper.getAllUsers();
        String activeId = prefHelper.getActiveUserId();

        if (userList.isEmpty()) {
            tvEmptyUserWarning.setVisibility(View.VISIBLE);
            return;
        }

        tvEmptyUserWarning.setVisibility(View.GONE);

        for (UserProfile u : userList) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_user_profile, llLoginUserContainer, false);

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
            itemTvActiveBadge.setText("TERAKHIR AKTIF");

            // Masuk aplikasi dengan akun ini
            llSelectUserArea.setOnClickListener(v -> loginSebagaiUser(u));

            // Edit Profil Akun
            itemBtnEdit.setOnClickListener(v -> tampilkanFormUser(u));

            // Hapus Akun
            itemBtnHapus.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Pengguna")
                        .setMessage("Apakah Anda yakin ingin menghapus data pengguna " + u.getNamaLengkap() + "? Riwayat membaca akan terhapus.")
                        .setPositiveButton("Ya, Hapus", (d, w) -> {
                            prefHelper.deleteUser(u.getId());
                            Toast.makeText(this, "Pengguna berhasil dihapus!", Toast.LENGTH_SHORT).show();
                            muatDaftarPengguna();
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            });

            llLoginUserContainer.addView(itemView);
        }
    }

    private void loginSebagaiUser(UserProfile user) {
        prefHelper.setActiveUserId(user.getId());
        Toast.makeText(this, "Selamat datang, " + user.getNamaLengkap() + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
            tvFormTitle.setText("Pendaftaran Pengguna Baru");
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
                dialog.dismiss();
                muatDaftarPengguna();
            } else {
                UserProfile newUser = new UserProfile(
                        UUID.randomUUID().toString(),
                        username,
                        namaLengkap,
                        ttl,
                        jk,
                        komitmen,
                        targetBab,
                        KitabData.CANON_KATOLIK
                );
                prefHelper.addUser(newUser);
                prefHelper.setActiveUserId(newUser.getId());
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loginSebagaiUser(newUser);
            }
        });

        dialog.show();
    }
}
