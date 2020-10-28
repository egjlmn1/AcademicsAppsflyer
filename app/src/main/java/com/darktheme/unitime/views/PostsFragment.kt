package com.darktheme.unitime.views

import PostsAdapter
import ScrollListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.JsonObjects.PostContentObj
import com.darktheme.unitime.models.JsonObjects.PostObj
import com.darktheme.unitime.models.MyViewHolder
import com.darktheme.unitime.viewModels.PostsViewModel


class PostsFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var viewsList : ArrayList<MyViewHolder>? = null
    var viewTypesList : ArrayList<Int>? = null

    var viewModel : PostsViewModel? = null

    var containerView: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        containerView = container
        val root = inflater.inflate(R.layout.fragment_posts, container, false)
        return root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        handleMVVM()

    }

    private fun handleMVVM() {
        viewModel = PostsViewModel(requireActivity(), recyclerView!!, viewsList!!, viewTypesList!!)
        viewModel!!.loadIdList()
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.posts_recycler_view)
        recyclerView!!.addOnScrollListener(ScrollListener())
        viewsList = ArrayList()
        viewTypesList = ArrayList()
        recyclerView!!.adapter = PostsAdapter(requireActivity(), viewsList!!, viewTypesList!!)
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())

    }

    companion object {
        val TAG = "PostsFragmentTAG"
    }
}