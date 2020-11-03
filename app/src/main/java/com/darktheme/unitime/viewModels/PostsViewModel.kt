package com.darktheme.unitime.viewModels

import android.app.Activity
import android.view.Gravity
import androidx.databinding.BaseObservable
import androidx.drawerlayout.widget.DrawerLayout
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.*
import com.darktheme.unitime.models.Retrofit.PostLoader
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.models.Room.Suggestion
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

abstract class PostsViewModel(val activity: Activity, posts: MutableList<PostObj>, refreshRV: (Int) -> Unit, addFolder: (String) -> Unit, onError: (String) -> Unit) : BaseObservable(),  MaterialSearchBar.OnSearchActionListener {

    private val postLoader: PostLoader =
        PostLoader(
            activity,
            posts,
            refreshRV,
            addFolder,
            onError
        )

    companion object {
        val TextType = "text"
        val ImageType = "image"
        val FileType = "file"
    }

    var inHierarchy = true

    fun onProfileClick() {
        val drawer = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer!!.openDrawer(Gravity.LEFT)

    }

    fun loadSuggestions(searchBar: MaterialSearchBar) {
        CoroutineScope(IO).launch {
            val suggestions = AppDataBase.getInstance(activity).suggestionDao().getSuggestionList()
            println("suggestions: ")
            for (sug in suggestions) {
                println(sug.suggestion)
            }
            //searchBar.setLastSuggestions(suggestions)
        }
    }

    fun saveSuggestions(suggestions: List<String>) {
        CoroutineScope(IO).launch {
            val db = AppDataBase.getInstance(activity).suggestionDao()
            for (sug in suggestions) {
                db.insertSuggestions(Suggestion(sug))
                println("suggestions saving: " + sug)
            }
            println("suggestions saved")
        }
    }

    override fun onButtonClicked(buttonCode: Int) {
        println("search button clicked")
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        println("search state changed")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        println("search confirmed: " + text)
    }

    fun changeView() {
        if (inHierarchy) {
            (activity as MainPageActivity).navController!!.navigate(R.id.action_nav_hierarchy_to_nav_best_fit)
        } else {
            (activity as MainPageActivity).navController!!.navigate(R.id.action_nav_best_fit_to_nav_hierarchy)
        }
    }

    abstract fun loadPosts()
}