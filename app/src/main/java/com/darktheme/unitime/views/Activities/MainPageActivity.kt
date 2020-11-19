package com.darktheme.unitime.views.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.ActivityMainPageBinding
import com.darktheme.unitime.models.Retrofit.JsonObjects.FlairObj
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.viewModels.MainPageViewModel
import com.darktheme.unitime.views.Login
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainPageActivity : AppCompatActivity() {

    var navController : NavController? = null
    var drawer: DrawerLayout? = null
    var navbar: BottomNavigationView? = null
    var context: Context? = null

    // data between fragments
    var email: String? = null
    var searchText: String? = null
    var searchFlair: FlairObj? = null
    var currentPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainPageBinding: ActivityMainPageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_page)
        activityMainPageBinding.viewModel = MainPageViewModel()
        context = this
        getEmail()
        initNavigations()
    }

    fun getEmail() {
        email = PreferenceManager.getDefaultSharedPreferences(context).getString(context!!.getString(R.string.current_email), null)
        if (email == null) {
            println("An error occurred (No Email In Prefs)")
        }
    }

    fun initNavigations() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        initDrawer()
        initBottomBar()
    }

    fun initDrawer() {
        drawer = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(drawerListener)
        val header = navView.getHeaderView(0)
        val profileName = header.findViewById<TextView>(R.id.profile_name)
        CoroutineScope(IO).launch {
            val db = AppDataBase.getInstance(context!!)
            if (email != null) {
                val profile = db.profileDao().getProfile(email!!)
                if (profile != null) {
                    withContext(Main) {
                        profileName.text = profile.name
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (this.drawer!!.isDrawerOpen(GravityCompat.START)) {
            this.drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun initBottomBar() {
        navbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navbar!!.isEnabled = true
        navbar!!.selectedItemId = R.id.home_label
        navbar!!.setOnNavigationItemSelectedListener(bottomBarListener)
    }

    val bottomBarListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home_label -> {
                    navController!!.navigate(R.id.action_to_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.new_post_label -> {
                    navController!!.navigate(R.id.action_to_nav_create_post)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.third_label -> {

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    val drawerListener =
        NavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    navController!!.navigate(R.id.action_to_nav_my_profile)
                    this.drawer!!.closeDrawer(GravityCompat.START)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_logout -> {
                    Login().logout(this)
                    val intent = Intent(this, StartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun showNavBar() {
        navbar!!.visibility = View.VISIBLE
    }

    fun hideNavBar() {
        navbar!!.visibility = View.GONE
    }
}