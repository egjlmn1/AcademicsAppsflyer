package com.darktheme.unitime.viewModels

import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BaseObservable

class MainPageViewModel : BaseObservable()  {

    var darkMode = false

    fun onDarkModeClick() {
        darkMode = !darkMode
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}