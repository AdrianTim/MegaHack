package com.infinityleaks.arphonecomparator.module;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_NAME = "PhoneComparator";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(final Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    public void remove(final String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void putString(final String key, final String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void putInt(final String key, final int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void putBoolean(final String key, final boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void putLong(final String key, final long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public void putFloat(final String key, final float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public String getString(final String key, final String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(final String key, final int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(final String key, final long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public float getFloat(final String key, final float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void saveToken(String token) {
        putString("token", token);
    }

    public String getToken() {
        return getString("token", null);
    }
}
