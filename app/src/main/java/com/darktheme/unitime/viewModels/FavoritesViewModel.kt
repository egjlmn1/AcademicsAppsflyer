package com.darktheme.unitime.viewModels

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.drawerlayout.widget.DrawerLayout
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.CustomViews.FolderView
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class FavoritesViewModel(val activity: Activity) : BaseObservable(),  MaterialSearchBar.OnSearchActionListener {

    var searchBar: MaterialSearchBar? = null
    val folders = ArrayList<FolderView>()

    fun onBackClick() {
        (activity as MainPageActivity).navController!!.navigate(R.id.action_to_nav_main_posts)
    }

    fun loadFolders(view: View, activity: MainPageActivity) {
        CoroutineScope(IO).launch {
            if (activity.email == null) {
                Toast.makeText(activity,"Try again later", Toast.LENGTH_SHORT).show();
                return@launch
            }
            val db = AppDataBase.getInstance(activity)
            val favorites = db.favoritesDao().getFoldersList(activity.email!!)
            if (favorites != null) {
                for (folderPath in favorites.folders!!) {
                    withContext(Dispatchers.Main) {
                        val container = view.findViewById<LinearLayout>(R.id.folders_container)
                        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


                        val addedFolder = inflater.inflate(R.layout.layout_single_folder, container, false);
                        folders.add(FolderView(addedFolder, folderPath, activity))
                        addedFolder.setOnClickListener {
                            activity.currentPath = folderPath
                            println("FOLDER PATH " + folderPath)
                            activity.navController!!.navigate(R.id.action_to_home)
                        }
                        container.addView(addedFolder, container.childCount - 1) // -1 cuz before the recycler view
                    }
                }
            }
        }
    }

    fun initSearchBar(view: View) {
        searchBar = view.findViewById<MaterialSearchBar>(R.id.searchBar)
        searchBar!!.setOnSearchActionListener(this)
    }

    override fun onButtonClicked(buttonCode: Int) {
        println("search button clicked")
    }

    override fun onSearchStateChanged(enabled: Boolean) {

        println("search state changed")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        println("search confirmed: " + text)
        for (folder in folders) {
            if (folder.path.toLowerCase(Locale.ROOT).startsWith(searchBar!!.text.toLowerCase(Locale.ROOT))) {
                folder.show()
            } else {
                folder.hide()
            }
        }
    }
}