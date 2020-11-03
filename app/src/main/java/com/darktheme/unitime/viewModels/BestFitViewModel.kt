package com.darktheme.unitime.viewModels

import android.app.Activity
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj

class BestFitViewModel(activity: Activity, posts: MutableList<PostObj>, refreshRV: (Int) -> Unit, addFolder: (String) -> Unit, onError: (String) -> Unit) : PostsViewModel(activity, posts, refreshRV, addFolder, onError) {
    override fun loadPosts() {
        //TODO("Not yet implemented")
    }
}