package com.darktheme.academics.viewModels

import android.app.Activity
import androidx.databinding.BaseObservable
import com.darktheme.academics.models.Retrofit.JsonObjects.PostObj
import com.darktheme.academics.models.Retrofit.PostLoader


class ProfileViewModel(val activity: Activity, posts: MutableList<PostObj>, refreshRV: (Int) -> Unit, addFolder: (String) -> Unit, onError: (String) -> Unit) : BaseObservable() {

    private val postLoader: PostLoader =
        PostLoader(
            activity,
            posts,
            refreshRV,
            addFolder,
            onError
        )

    fun loadPosts(email: String) {
        postLoader.myIdList(email){}
    }

    fun onBackPress() {
        activity.onBackPressed()
    }
}