package com.darktheme.unitime.viewModels

import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BaseObservable
import com.darktheme.unitime.R

class MainPageViewModel(val context: Context) : BaseObservable()  {

    var darkMode: Boolean
    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        darkMode = prefs.getBoolean("darkmode", true)
    }

    fun setMode() {
        if (!darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().putBoolean("darkmode", darkMode).apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().putBoolean("darkmode", darkMode).apply()
        }
    }

    fun onDarkModeClick() {
        darkMode = !darkMode
        setMode()
    }

}