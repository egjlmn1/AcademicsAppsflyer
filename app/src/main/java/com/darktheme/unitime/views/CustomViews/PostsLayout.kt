package com.darktheme.unitime.views.CustomViews

import PostsAdapter
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BaseObservable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnFileClickListener
import com.darktheme.unitime.OnPostClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity

open class PostsLayout(val view: View, val activity: MainPageActivity) {
    var recyclerView: RecyclerView? = null
    var posts : ArrayList<PostObj> = ArrayList()
    var viewModel: PostsViewModel? = null


    open fun createViewModel(activity : MainPageActivity): BaseObservable {
        viewModel = PostsViewModel(activity, posts, refreshRV, addFolder, onError)
        return viewModel!!
    }

    val addFolder = {folderPath: String ->
        val container = view.findViewById<LinearLayout>(R.id.posts_container)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val addedFolder = inflater.inflate(R.layout.layout_single_folder, container, false)
        FolderView(addedFolder, folderPath, activity)
        addedFolder.setOnClickListener {
            refreshRecyclerView()
            activity.currentPath = folderPath
            viewModel!!.loadFolder(activity.currentPath)
            if (folderPath.isNotEmpty()) {
                val splited = folderPath.split("/")
                view.findViewById<TextView>(R.id.folder).text = splited[splited.lastIndex]
            } else {
                view.findViewById<TextView>(R.id.folder).text = ""
            }

        }
        container.addView(addedFolder, container.childCount - 1) // -1 cuz before the recycler view
    }

    val onError = {errorMsg: String ->
        val container = view.findViewById<LinearLayout>(R.id.posts_container)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val errorLayout = inflater.inflate(R.layout.layout_error_loading, null, false)
        errorLayout.findViewById<TextView>(R.id.error_text).text = errorMsg
        container.addView(errorLayout, container.childCount - 1) // -1 cuz before the recycler view
    }

    val refreshRV = {position : Int ->
        recyclerView!!.adapter!!.notifyItemInserted(position)

        recyclerView!!.adapter!!.notifyItemRangeChanged(position, (posts.size - 1) - position)
    }


    fun initRecyclerView(activity: Activity) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        //recyclerView!!.addOnScrollListener(ScrollListener())
        recyclerView!!.adapter = PostsAdapter(activity as MainPageActivity, posts, OnPostClickListener((activity as MainPageActivity).navController!!), OnFileClickListener(view.context))
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
    }

    fun refreshRecyclerView() {
        val size = posts.size
        posts.clear()
        recyclerView!!.adapter!!.notifyItemRangeRemoved(0, size)

        val container = view.findViewById<LinearLayout>(R.id.posts_container)
        if (container.childCount > 2) {
            container.removeViews(1, container.childCount-2)
        }
    }
}