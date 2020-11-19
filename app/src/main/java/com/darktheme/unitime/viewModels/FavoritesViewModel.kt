package com.darktheme.unitime.viewModels

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
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

class FavoritesViewModel(val activity: Activity) : BaseObservable(),  MaterialSearchBar.OnSearchActionListener {

    var searchBar: MaterialSearchBar? = null
    val folders = ArrayList<FolderView>()

    fun onProfileClick() {
        val drawer = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer!!.openDrawer(Gravity.LEFT)
    }

    fun loadFolders(view: View, activity: MainPageActivity) {
        CoroutineScope(IO).launch {
            val db = AppDataBase.getInstance(activity)
            val favorites = db.favoritesDao().getFoldersList(activity.email!!)
            if (favorites != null) {
                for (folderPath in favorites) {
                    withContext(Dispatchers.Main) {
                        val container = view.findViewById<LinearLayout>(R.id.posts_container)
                        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                        val addedFolder = inflater.inflate(R.layout.layout_single_folder, null, false);
                        folders.add(FolderView(addedFolder, folderPath, activity))
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
        //TODO check if takes to much time: put in coroutine
        for (folder in folders) {
            if (folder.path.startsWith(searchBar!!.text)) {
                folder.show()
            } else {
                folder.hide()
            }
        }
        println("search state changed")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        println("search confirmed: " + text)
    }
}