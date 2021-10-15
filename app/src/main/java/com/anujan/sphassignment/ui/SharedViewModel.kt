package com.anujan.sphassignment.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.anujan.sphassignment.util.loadLoginSharedPrefState
import com.anujan.sphassignment.util.loadNightModeState
import com.anujan.sphassignment.util.loginSharedPrefState
import com.anujan.sphassignment.util.setNightModeState
import javax.inject.Inject

class SharedViewModel @Inject constructor(
    private var sharedPreferences: SharedPreferences
):ViewModel() {

    fun loggedInOrNot(): Boolean {
        return sharedPreferences.loadLoginSharedPrefState()
    }
    fun setLogout(){
        sharedPreferences.loginSharedPrefState(false)
    }
    fun setThemeMode(state : Boolean){
        sharedPreferences.setNightModeState(state)
    }
    fun loadThemeMode(): Boolean {
       return sharedPreferences.loadNightModeState()
    }
}