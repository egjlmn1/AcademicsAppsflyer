package com.darktheme.academics.viewModels

import android.preference.PreferenceManager
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.databinding.BaseObservable
import androidx.drawerlayout.widget.DrawerLayout
import com.darktheme.academics.R
import com.darktheme.academics.models.Retrofit.JsonObjects.FlairObj
import com.darktheme.academics.models.Retrofit.JsonObjects.PostObj
import com.darktheme.academics.models.Retrofit.PostLoader
import com.darktheme.academics.views.Activities.MainPageActivity
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsViewModel(val activity: MainPageActivity, posts: MutableList<PostObj>, refreshRV: (Int) -> Unit, addFolder: (String) -> Unit, onError: (String) -> Unit) : BaseObservable(),  MaterialSearchBar.OnSearchActionListener {

    private val postLoader: PostLoader =
        PostLoader(
            activity,
            posts,
            refreshRV,
            addFolder,
            onError
        )

    companion object {
        val ImageType = "image"
        val FileType = "file"
    }

    fun onProfileClick() {
        val drawer = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer!!.openDrawer(GravityCompat.START)
    }

    override fun onButtonClicked(buttonCode: Int) {
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        println("search state changed")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        println("search confirmed: " + text)
        CoroutineScope(IO).launch {
            loadFlair()
            activity.searchText = text.toString()
            activity.navController!!.navigate(R.id.action_to_nav_search_result)
        }
    }

    fun loadFlair() {
        val flair = FlairObj(
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("question", true),
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("suggestion", true),
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("test", true),
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("summary", true),
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("social", true)
        )
        activity.searchFlair = flair
    }

    fun loadPosts(bestFit: Boolean, path: String, onResult: () -> Unit) {
        CoroutineScope(IO).launch {
            loadFlair()
            if (bestFit) {
                    withContext(Main) {
                        postLoader.bestfitIdList("email", activity.searchFlair, onResult)
                    }
            } else {
                postLoader.foldersIdList(path, activity.searchFlair, onResult)
            }
        }
    }

    fun loadFolder(path: String) {
        CoroutineScope(IO).launch {
            loadFlair()
            withContext(Main) {
                postLoader.foldersIdList(path, activity.searchFlair){}
            }
        }

    }

    fun searchPosts(search: String, flair: FlairObj, onResult: () -> Unit) {
        postLoader.searchIdList(search, flair, onResult)
    }
}