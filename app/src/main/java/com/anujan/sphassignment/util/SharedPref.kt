package com.anujan.sphassignment.util

import android.content.SharedPreferences
import com.anujan.sphassignment.util.SharedPreferencesData

fun SharedPreferences.loadNightModeState(): Boolean {
    return getBoolean(SharedPreferencesData.NIGHT_MODE, false)
}
fun SharedPreferences.setNightModeState(state: Boolean) {
    edit()
    .putBoolean(SharedPreferencesData.NIGHT_MODE, state).apply()
}

fun SharedPreferences.loadLoginSharedPrefState(): Boolean {
    return getBoolean(SharedPreferencesData.LOGGED_IN, false)
}

fun SharedPreferences.loginSharedPrefState(state: Boolean) {
    edit()
        .putBoolean(SharedPreferencesData.LOGGED_IN, state)
        .apply()
}
