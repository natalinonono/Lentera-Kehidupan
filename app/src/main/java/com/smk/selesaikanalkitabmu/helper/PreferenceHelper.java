package com.smk.selesaikanalkitabmu.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PreferenceHelper {
    private static final String PREF_NAME = "selesaikan_alkitabmu_pref";
    private static final String KEY_STREAK = "key_streak";
    private static final String KEY_LAST_READ_DATE = "key_last_read_date";
    private static final String KEY_CURRENT_PASAL_INDEX = "key_current_pasal_index"; // mulai dari 1
    private static final String KEY_CURRENT_AYAT_IN_PASAL = "key_current_ayat_in_pasal"; // ayat berikutnya yang harus dibaca, mulai dari 1
    private static final String KEY_TOTAL_AYAT_READ = "key_total_ayat_read"; // akumulasi total ayat yang sudah dibaca
    private static final String KEY_DAILY_TARGET_COUNT = "key_daily_target_count"; // default misal 3 pasal atau 15 ayat
    private static final String KEY_TARGET_UNIT = "key_target_unit"; // "pasal" atau "ayat"
    private static final String KEY_FIRST_TIME_SETUP = "key_first_time_setup";
    private static final String KEY_CANON_TYPE = "key_canon_type";
    private static final String KEY_HISTORY = "key_history";

    private final SharedPreferences prefs;
    private final Gson gson;

    public PreferenceHelper(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public boolean isFirstTimeSetup() {
        return prefs.getBoolean(KEY_FIRST_TIME_SETUP, true);
    }

    public void setFirstTimeSetupDone() {
        prefs.edit().putBoolean(KEY_FIRST_TIME_SETUP, false).apply();
    }

    public String getTargetUnit() {
        return prefs.getString(KEY_TARGET_UNIT, "pasal");
    }

    public void setTargetUnit(String unit) {
        prefs.edit().putString(KEY_TARGET_UNIT, unit).apply();
    }

    public String getCanonType() {
        return prefs.getString(KEY_CANON_TYPE, "katolik");
    }

    public void setCanonType(String type) {
        prefs.edit().putString(KEY_CANON_TYPE, type).apply();
    }

    public int getStreak() {
        return prefs.getInt(KEY_STREAK, 0);
    }

    public void setStreak(int streak) {
        prefs.edit().putInt(KEY_STREAK, streak).apply();
    }

    public String getLastReadDate() {
        return prefs.getString(KEY_LAST_READ_DATE, "");
    }

    public void setLastReadDate(String dateStr) {
        prefs.edit().putString(KEY_LAST_READ_DATE, dateStr).apply();
    }

    public int getCurrentPasalIndex() {
        return prefs.getInt(KEY_CURRENT_PASAL_INDEX, 1);
    }

    public void setCurrentPasalIndex(int index) {
        prefs.edit().putInt(KEY_CURRENT_PASAL_INDEX, index).apply();
    }

    public int getCurrentAyatInPasal() {
        return prefs.getInt(KEY_CURRENT_AYAT_IN_PASAL, 1);
    }

    public void setCurrentAyatInPasal(int ayat) {
        prefs.edit().putInt(KEY_CURRENT_AYAT_IN_PASAL, ayat).apply();
    }

    public int getTotalAyatRead() {
        return prefs.getInt(KEY_TOTAL_AYAT_READ, 0);
    }

    public void setTotalAyatRead(int total) {
        prefs.edit().putInt(KEY_TOTAL_AYAT_READ, total).apply();
    }

    public int getDailyTargetCount() {
        return prefs.getInt(KEY_DAILY_TARGET_COUNT, 3);
    }

    public void setDailyTargetCount(int count) {
        prefs.edit().putInt(KEY_DAILY_TARGET_COUNT, count).apply();
    }

    public boolean isReadToday() {
        String today = getTodayDate();
        return today.equals(getLastReadDate());
    }

    public String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long oneDayMillis = 24 * 60 * 60 * 1000L;
        return sdf.format(new Date(System.currentTimeMillis() - oneDayMillis));
    }

    /**
     * Memperbarui streak dan riwayat
     */
    public void recordCompletion(String rangeBacaan) {
        String today = getTodayDate();
        String yesterday = getYesterdayDate();
        String lastDate = getLastReadDate();

        int currentStreak = getStreak();

        if (today.equals(lastDate)) {
            return;
        }

        if (yesterday.equals(lastDate)) {
            currentStreak += 1;
        } else {
            currentStreak = 1;
        }

        setStreak(currentStreak);
        setLastReadDate(today);

        // Tambah riwayat
        addHistory("Selesai: " + rangeBacaan + " (" + today + ")");
    }

    public void addHistory(String entry) {
        List<String> list = getHistoryList();
        list.add(0, entry);
        if (list.size() > 50) {
            list = list.subList(0, 50);
        }
        String json = gson.toJson(list);
        prefs.edit().putString(KEY_HISTORY, json).apply();
    }

    public List<String> getHistoryList() {
        String json = prefs.getString(KEY_HISTORY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void resetAll() {
        prefs.edit().clear().apply();
    }
}
