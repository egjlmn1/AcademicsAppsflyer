package com.darktheme.academics.views.CustomViews

import android.view.View
import androidx.databinding.BaseObservable
import com.darktheme.academics.viewModels.ProfileViewModel
import com.darktheme.academics.views.Activities.MainPageActivity

class ProfileLayout(view: View, activity: MainPageActivity): PostsLayout(view, activity) {
    override fun createViewModel(activity: MainPageActivity): BaseObservable {
        return ProfileViewModel(activity, posts, refreshRV, addFolder, onError)
    }
}