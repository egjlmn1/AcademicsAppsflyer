package com.darktheme.unitime.views.Fragments

import PostsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.darktheme.unitime.views.CustomViews.BestFitLayout
import com.darktheme.unitime.views.CustomViews.PostsLayout
import com.mancj.materialsearchbar.MaterialSearchBar


//TODO replace the postFragment with 3 new fragments: best fit fragment, search fragment, hierarchy fragment and have all three of them include this layout
class BestFitFragment : Fragment() {

    var posts : PostsLayout? = null
    var containerView: ViewGroup? = null
    var viewModel : PostsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        containerView = container
        val binding: FragmentPostsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_posts, container, false
        )
        val view: View = binding.getRoot()

        val activity = requireActivity() as MainPageActivity
        activity.showNavBar()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity.finish()
                }
            }
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        posts = BestFitLayout(view)
        viewModel = posts!!.createViewModel(requireActivity())
        viewModel!!.inHierarchy = false
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        posts!!.initAll(requireActivity())


    }

    override fun onDestroy() {
        super.onDestroy()
        posts!!.onDestroy()
    }
}