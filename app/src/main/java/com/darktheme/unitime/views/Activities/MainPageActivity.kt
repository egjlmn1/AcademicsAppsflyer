package com.darktheme.unitime.views.Activities

import android.app.SearchManager
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.ActivityMainPageBinding
import com.darktheme.unitime.viewModels.MainPageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainPageActivity : AppCompatActivity() {

    var navController : NavController? = null
    var drawer: DrawerLayout? = null
    var navbar: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main_page)

        val activityMainPageBinding: ActivityMainPageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_page)
        activityMainPageBinding.viewModel = MainPageViewModel()

        setNavigations()

    }

    fun setNavigations() {
        drawer = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        navView.setNavigationItemSelectedListener(drawerListener)

        setBottomBar()
    }
    override fun onBackPressed() {
        if (this.drawer!!.isDrawerOpen(GravityCompat.START)) {
            this.drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setBottomBar() {
        navbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navbar!!.isEnabled = true
        navbar!!.selectedItemId = R.id.home_label
        navbar!!.setOnNavigationItemSelectedListener(bottomBarListener)
    }

    val bottomBarListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            println("on item menu click")
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
            println("on item menu click")
            when (item.itemId) {
                R.id.nav_profile -> {
                    println("nav profile click")
                    navController!!.navigate(R.id.action_nav_posts_to_nav_my_profile)
                    this.drawer!!.closeDrawer(GravityCompat.START)
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