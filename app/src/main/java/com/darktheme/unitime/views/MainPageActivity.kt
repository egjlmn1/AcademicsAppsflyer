package com.darktheme.unitime.views

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.darktheme.unitime.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainPageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        setNavigations()
        //openNewFragment(PostsFragment())
        handleIntent(intent)

    }

    fun setNavigations() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setBottomBar()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        println("onSupportNavigateUp")
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                println("A search occured: " + query.toString())
            }
        }
    }

    private fun setBottomBar() {
        println("setting nav bar")
        val navbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navbar.isEnabled = true
        navbar.selectedItemId = R.id.home_label
        navbar.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            println("checking id")
            when (item.itemId) {
                R.id.home_label -> {
                    println("going home")
                    onCreatePostDiselect()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.new_post_label -> {
                    onCreatePostSelect()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.third_label -> {
                    getCurrentFragment()
                    onCreatePostDiselect()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun onCreatePostSelect() {
        println("creating post")
        val createPostFragment = CreatePostFragment()
        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, createPostFragment, CreatePostFragment.TAG)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onCreatePostDiselect() {
        val frag: Fragment? = supportFragmentManager.findFragmentByTag(CreatePostFragment.TAG)
        if (frag != null) {
            println("deleting post")
            val transaction = supportFragmentManager.beginTransaction().remove(frag).commit()
            supportFragmentManager.popBackStack()
        }
    }

    fun getCurrentFragment() {
        val frag: Fragment? = supportFragmentManager.findFragmentByTag(CreatePostFragment.TAG)
        println(frag)
    }
}