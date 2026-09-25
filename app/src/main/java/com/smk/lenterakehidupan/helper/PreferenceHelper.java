package com.smk.lenterakehidupan.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smk.lenterakehidupan.model.KitabData;
import com.smk.lenterakehidupan.model.ReadingHistory;
import com.smk.lenterakehidupan.model.UserProfile;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PreferenceHelper {
    private static final String PREF_NAME = "AlkitabPref";
    private static final String KEY_USER_LIST = "key_user_profiles_v2";
    private static final String KEY_ACTIVE_USER_ID = "key_active_user_id";

    // Legacy keys for backward compatibility
    private static final String LEGACY_KEY_PASAL = "pasal";
    private static final String LEGACY_KEY_STREAK = "streak";
    private static final String LEGACY_KEY_LAST_DATE = "lastDate";
    private static final String LEGACY_KEY_CANON = "canon";
    private static final String LEGACY_KEY_USER_NAME = "user_name";

    private final SharedPreferences prefs;
    private final Gson gson;

    public PreferenceHelper(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long oneDayMillis = 24 * 60 * 60 * 1000L;
        return sdf.format(new Date(System.currentTimeMillis() - oneDayMillis));
    }

    // ==================== CRUD USER ====================

    public List<UserProfile> getAllUsers() {
        String json = prefs.getString(KEY_USER_LIST, null);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<UserProfile>>() {}.getType();
        List<UserProfile> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public void saveAllUsers(List<UserProfile> users) {
        String json = gson.toJson(users);
        prefs.edit().putString(KEY_USER_LIST, json).apply();
    }

    public String getActiveUserId() {
        return prefs.getString(KEY_ACTIVE_USER_ID, "");
    }

    public void setActiveUserId(String userId) {
        prefs.edit().putString(KEY_ACTIVE_USER_ID, userId).apply();
    }

    public UserProfile getActiveUser() {
        String activeId = getActiveUserId();
        List<UserProfile> users = getAllUsers();
        for (UserProfile u : users) {
            if (u.getId().equals(activeId)) {
                return u;
            }
        }
        if (!users.isEmpty()) {
            setActiveUserId(users.get(0).getId());
            return users.get(0);
        }
        return null;
    }

    public void updateActiveUser(UserProfile updatedProfile) {
        List<UserProfile> users = getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedProfile.getId())) {
                users.set(i, updatedProfile);
                saveAllUsers(users);
                return;
            }
        }
    }

    public void addUser(UserProfile newUser) {
        if (newUser.getId() == null || newUser.getId().isEmpty()) {
            newUser.setId(UUID.randomUUID().toString());
        }
        List<UserProfile> users = getAllUsers();
        users.add(newUser);
        saveAllUsers(users);
    }

    public boolean deleteUser(String userId) {
        List<UserProfile> users = getAllUsers();
        UserProfile toRemove = null;
        for (UserProfile u : users) {
            if (u.getId().equals(userId)) {
                toRemove = u;
                break;
            }
        }
        if (toRemove != null) {
            users.remove(toRemove);
            saveAllUsers(users);
            if (getActiveUserId().equals(userId)) {
                if (!users.isEmpty()) {
                    setActiveUserId(users.get(0).getId());
                } else {
                    setActiveUserId("");
                }
            }
            return true;
        }
        return false;
    }

    // ==================== LOGIKA PERJALANAN & LOSE STREAK ====================

    /**
     * Memeriksa dan memperbarui status Lose Streak pada user saat aplikasi dimuat.
     * Jika hari terakhir membaca adalah sebelum kemarin (dan tidak kosong), berarti streak putus.
     */
    public boolean checkAndApplyLoseStreak(UserProfile user) {
        String lastDate = user.getLastReadDate();
        if (lastDate == null || lastDate.isEmpty()) {
            return false;
        }
        String today = getTodayDate();
        String yesterday = getYesterdayDate();

        // Jika bukan hari ini dan bukan kemarin, serta streak > 0, berarti terjadi lose streak!
        if (!today.equals(lastDate) && !yesterday.equals(lastDate) && user.getStreak() > 0) {
            user.incrementLoseStreak();
            user.setStreak(0);
            updateActiveUser(user);
            return true;
        }
        return false;
    }

    /**
     * Menyelesaikan bacaan hari ini untuk active user sesuai target bab hariannya.
     * Mengembalikan informasi rentang bacaan yang telah diselesaikan.
     */
    public String selesaikanBacaanHariIni(UserProfile user) {
        int totalPasal = KitabData.getTotalPasal(user.getCanon());
        int targetBab = user.getTargetBabPerHari();
        int startPasal = user.getCurrentPasal();

        // Hitung rentang teks
        String rentangTeks = KitabData.getRentangBacaan(startPasal, targetBab, user.getCanon());

        // Hitung pasal akhir baru
        int endPasal = Math.min(startPasal + targetBab, totalPasal + 1);
        user.setCurrentPasal(Math.min(endPasal, totalPasal));

        // Kalkulasi Streak & Lose Streak
        String today = getTodayDate();
        String yesterday = getYesterdayDate();
        String lastDate = user.getLastReadDate();

        boolean isLoseStreakRecovery = false;
        if (yesterday.equals(lastDate)) {
            user.setStreak(user.getStreak() + 1);
        } else if (today.equals(lastDate)) {
            // Sudah selesai hari ini
            return rentangTeks;
        } else {
            // Jika sebelumnya bolong atau baru mulai
            if (user.getStreak() == 0 && user.getLoseStreakCount() > 0) {
                isLoseStreakRecovery = true;
            }
            user.setStreak(1);
        }

        user.setLastReadDate(today);

        // Catat ke riwayat perjalanan
        ReadingHistory history = new ReadingHistory(
                today,
                rentangTeks,
                targetBab,
                user.getPersentaseSelesai(),
                user.getStreak(),
                isLoseStreakRecovery
        );
        user.getHistoryList().add(0, history);

        // Batasi 100 riwayat terakhir
        if (user.getHistoryList().size() > 100) {
            user.setHistoryList(new ArrayList<>(user.getHistoryList().subList(0, 100)));
        }

        updateActiveUser(user);
        return rentangTeks;
    }

    public void resetProgress(UserProfile user) {
        user.setCurrentPasal(1);
        user.setStreak(0);
        user.setLastReadDate("");
        user.setHistoryList(new ArrayList<>());
        user.setLoseStreakCount(0);
        updateActiveUser(user);
    }
}
