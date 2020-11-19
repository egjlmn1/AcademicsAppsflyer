package com.darktheme.unitime.views.Fragments

import PostsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnPostClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentPostsBinding
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.CustomViews.PostsLayout
import com.darktheme.unitime.views.CustomViews.ProfileLayout
import com.mancj.materialsearchbar.MaterialSearchBar


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
        if ((myActivity!!.searchText == null) || (myActivity!!.searchFlair == null)) {
            //TODO show error message
            return
        }
        viewModel!!.searchPosts(myActivity!!.searchText!!, myActivity!!.searchFlair!!)
    }

    override fun initLayout() {
        // do something
        //called once
    }

    override fun setLayout() {
        // do something
        //called every time
    }
}