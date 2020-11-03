package com.darktheme.unitime.views.CustomViews

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.darktheme.unitime.R
import com.darktheme.unitime.viewModels.BestFitViewModel
import com.darktheme.unitime.viewModels.PostsViewModel

class BestFitLayout(view: View) : PostsLayout(view) {

    override fun createViewModel(activity : Activity): PostsViewModel? {
        viewModel = BestFitViewModel(activity, posts, refreshRV, addFolder, onError)
        return viewModel
    }

    override fun initLayout() {

        view.findViewById<Button>(R.id.bestfit_hierarchy_btn).text = "Best Fit View"
        view.findViewById<TextView>(R.id.change_to).text = "change to folders view"
    }
}