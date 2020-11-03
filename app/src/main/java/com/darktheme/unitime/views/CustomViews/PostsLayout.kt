package com.darktheme.unitime.views.CustomViews

import PostsAdapter
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnFileClickListener
import com.darktheme.unitime.OnPostClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.mancj.materialsearchbar.MaterialSearchBar
import java.security.AccessController.getContext

abstract class PostsLayout(val view: View) {
    var recyclerView: RecyclerView? = null
    var searchBar: MaterialSearchBar? = null
    var posts : ArrayList<PostObj> = ArrayList()

    var viewModel : PostsViewModel? = null


    fun initSearchBar() {
        searchBar = view.findViewById<MaterialSearchBar>(R.id.searchBar)
        searchBar!!.setOnSearchActionListener(viewModel)

        viewModel!!.loadSuggestions(searchBar!!)
    }

    fun initPosts() {
        viewModel!!.loadPosts()
    }

    abstract fun createViewModel(activity : Activity): PostsViewModel?

    val addFolder = {folderName: String ->
        val container = view.findViewById<LinearLayout>(R.id.posts_container)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val addedFolder = inflater.inflate(R.layout.layout_posts_hierarchy, null, false);
        container.addView(addedFolder, container.childCount - 2)
    }

    val onError = {errorMsg: String ->
        val container = view.findViewById<LinearLayout>(R.id.posts_container)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val errorLayout = inflater.inflate(R.layout.layout_error_loading, null, false);
        errorLayout.findViewById<TextView>(R.id.error_text).text = errorMsg
        container.addView(errorLayout, container.childCount - 2)
    }

    fun initRecyclerView(activity: Activity) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        //recyclerView!!.addOnScrollListener(ScrollListener())
        recyclerView!!.adapter = PostsAdapter(activity, posts, OnPostClickListener((activity as MainPageActivity).navController!!), OnFileClickListener(view.context))
        recyclerView!!.layoutManager = LinearLayoutManager(activity)

    }

    fun onDestroy() {
        viewModel!!.saveSuggestions(searchBar!!.getLastSuggestions() as List<String>);
    }

    val refreshRV = {position : Int ->
        recyclerView!!.adapter!!.notifyItemInserted(position)

        recyclerView!!.adapter!!.notifyItemRangeChanged(position, (posts.size - 1) - position)

        //recyclerView.smoothScrollToPosition(posts.size)
    }

    abstract fun initLayout()
    fun initAll(activity: Activity) {
        initLayout()
        initRecyclerView(activity)
        initSearchBar()
        initPosts()
    }
}