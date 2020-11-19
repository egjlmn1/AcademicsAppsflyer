package com.darktheme.unitime.viewModels

import PostsAdapter
import android.app.Activity
import android.view.Gravity
import android.view.View
import androidx.databinding.BaseObservable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnFileClickListener
import com.darktheme.unitime.OnPostClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.*
import com.darktheme.unitime.models.Retrofit.PostLoader
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.models.Room.Profile
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

class ProfileViewModel(val activity: Activity, posts: MutableList<PostObj>, refreshRV: (Int) -> Unit, addFolder: (String) -> Unit, onError: (String) -> Unit) : BaseObservable() {

    private val postLoader: PostLoader =
        PostLoader(
            activity,
            posts,
            refreshRV,
            addFolder,
            onError
        )

    fun loadPosts() {
        postLoader.myIdList("email")
    }

    fun onBackPress() {
        activity.onBackPressed()
    }
}