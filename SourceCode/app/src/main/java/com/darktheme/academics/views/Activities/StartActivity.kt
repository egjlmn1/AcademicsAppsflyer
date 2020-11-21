package com.darktheme.academics.views.Activities

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.darktheme.academics.R
import com.darktheme.academics.models.Retrofit.JsonObjects.LoginRequest
import com.darktheme.academics.models.Retrofit.JsonObjects.ProfileRetrofit
import com.darktheme.academics.models.Retrofit.RetrofitClient
import com.darktheme.academics.views.Login
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


class StartActivity : AppCompatActivity() {

    var navController : NavController? = null

    var email = ""
    var password1  = ""
    var password2  = ""
    var name = ""


    fun loginRequest(email: String, password: String, loginResponse: (call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>?) -> Unit, loginFailure: (Call<ProfileRetrofit>?, Throwable?)->Unit) {
        println("login1")
        LoginRequest(
            RetrofitClient.getInstance()!!
        ).post(email.toLowerCase(), password, loginResponse, loginFailure)
    }

    fun loginProfile(profile: ProfileRetrofit, action: Int) {
        val contex = this
        CoroutineScope(IO).launch {
            println("id: " + profile.user_id)
            Login().login(contex, profile.toDBProfile())
            withContext(Main) {
                navController!!.navigate(action)
            }
        }
    }

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