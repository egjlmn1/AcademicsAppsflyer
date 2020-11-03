package com.darktheme.unitime.views

import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.darktheme.unitime.R

class Login {

    fun login(contex: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(contex)
        prefs.edit().putBoolean(contex.getString(R.string.logged_in), true).apply()
    }

    fun logout(contex: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(contex)
        prefs.edit().putBoolean(contex.getString(R.string.logged_in), false).apply()
    }
}