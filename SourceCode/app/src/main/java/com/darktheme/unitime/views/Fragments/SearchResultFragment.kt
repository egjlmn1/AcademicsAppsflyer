package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentPostsBinding
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.CustomViews.PostsLayout


class SearchResultFragment : MainPostsFragment() {

    var myActivity: MainPageActivity? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        containerView = container
        val binding: FragmentPostsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_posts, container, false
        )
        val view: View = binding.getRoot()
        myActivity = requireActivity() as MainPageActivity
        myActivity!!.showNavBar()

        posts = PostsLayout(view, myActivity!!)
        viewModel = posts!!.createViewModel(requireActivity() as MainPageActivity) as PostsViewModel
        binding.viewModel = viewModel
        return view
    }

    override fun loadPosts() {
        swipeContainer!!.setRefreshing(true)
        viewModel!!.searchPosts(myActivity!!.searchText, myActivity!!.searchFlair){ swipeContainer!!.setRefreshing(false);}
    }

    override fun initLayout() {
        // do something
        val view = LayoutInflater.from(context).inflate(R.layout.layout_show_search, null, false)

        requireView().findViewById<RelativeLayout>(R.id.extra_container).addView(view, 0)
    }

    override fun setLayout() {
        // do something
        //called every time
        requireView().findViewById<TextView>(R.id.search_show).text = "Showing search result for: \"" + myActivity!!.searchText + "\""
        val layout =  requireView().findViewById<LinearLayout>(R.id.posts_container)
        if (layout.childCount > 2) {
            layout.removeViewAt(1)
        }
    }
}