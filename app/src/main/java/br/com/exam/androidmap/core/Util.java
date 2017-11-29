package br.com.exam.androidmap.core;

import android.content.SharedPreferences;

public class Util {

    public static SharedPreferences.Editor putPreferencesDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getPreferencesDouble(final SharedPreferences prefs, final String key) {
        long prefsLong = prefs.getLong(key, 0);
        return Double.longBitsToDouble(prefsLong);
    }

}
