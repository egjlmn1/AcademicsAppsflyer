package com.darktheme.unitime.views.CustomViews

import PostsAdapter
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BaseObservable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.darktheme.unitime.OnFileClickListener
import com.darktheme.unitime.OnPostClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.viewModels.ProfileViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.mancj.materialsearchbar.MaterialSearchBar

class ProfileLayout(view: View, activity: MainPageActivity): PostsLayout(view, activity) {
    override fun createViewModel(activity: MainPageActivity): BaseObservable {
        return ProfileViewModel(activity, posts, refreshRV, addFolder, onError)
    }
}