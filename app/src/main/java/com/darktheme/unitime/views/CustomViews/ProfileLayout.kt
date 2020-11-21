package com.darktheme.unitime.views.CustomViews

import android.view.View
import androidx.databinding.BaseObservable
import com.darktheme.unitime.viewModels.ProfileViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity

class ProfileLayout(view: View, activity: MainPageActivity): PostsLayout(view, activity) {
    override fun createViewModel(activity: MainPageActivity): BaseObservable {
        return ProfileViewModel(activity, posts, refreshRV, addFolder, onError)
    }
}