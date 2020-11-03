package com.darktheme.unitime.views.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.ActivityStartBinding
import com.darktheme.unitime.viewModels.LoginViewModel
import com.darktheme.unitime.viewModels.RegisterViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception


class StartActivity : AppCompatActivity() {

    var navController : NavController? = null

    var email = ""
    var password1  = ""
    var password2  = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        checkLoggedIn()

        setNavigations()
    }

    fun checkLoggedIn() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val loggedIn = prefs.getBoolean(getString(R.string.logged_in), false)
        println(loggedIn)
    }

    override fun onBackPressed() {
        val text = findViewById<TextInputEditText>(R.id.edittext1)
        if (text.isFocused) {
            text.clearFocus()
            return
        }
        try {
            val text2 = findViewById<TextInputEditText>(R.id.edittext2)
            if (text2.isFocused) {
                text2.clearFocus()
                return
            }
        } catch (e: Exception) {}
        super.onBackPressed()
    }

    fun setNavigations() {

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

    }
}