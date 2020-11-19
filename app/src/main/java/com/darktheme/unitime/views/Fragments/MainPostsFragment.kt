package com.darktheme.unitime.views.Fragments

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentPostsBinding
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.Adapters.MySpinnerAdapter
import com.darktheme.unitime.views.Adapters.SpinnerCheckBox
import com.darktheme.unitime.views.CustomViews.PostsLayout
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//TODO replace the postFragment with 3 new fragments: best fit fragment, search fragment, hierarchy fragment and have all three of them include this layout
open class MainPostsFragment : Fragment() {

    var posts : PostsLayout? = null
    var containerView: ViewGroup? = null
    var viewModel : PostsViewModel? = null

    var searchBar: MaterialSearchBar? = null


    var bestFit: Boolean = false
    var positionInFolders: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        containerView = container
        val binding: FragmentPostsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_posts, container, false
        )
        val view: View = binding.getRoot()

        val activity = requireActivity() as MainPageActivity
        activity.showNavBar()

        posts = PostsLayout(view, requireActivity() as MainPageActivity)
        viewModel = posts!!.createViewModel(requireActivity() as MainPageActivity) as PostsViewModel
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
    }

    fun initAll() {
        initLayout()
        setLayout()
        initFlairs()
        posts!!.initRecyclerView(requireActivity())
        initRefresh()
        initSearchBar()
        loadPosts()
    }

    open fun loadPosts() {
        viewModel!!.loadPosts(bestFit)
    }

    fun initRefresh() {
        val swipeContainer = requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        swipeContainer.setOnRefreshListener {
            refreshAll()
            swipeContainer.setRefreshing(false);
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
    }

    fun initSearchBar() {
        searchBar = requireView().findViewById<MaterialSearchBar>(R.id.searchBar)
        searchBar!!.setOnSearchActionListener(viewModel)
        //viewModel!!.loadSuggestions(searchBar!!)
    }

    open fun initLayout() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_change_view, null, false)
        requireView().findViewById<RelativeLayout>(R.id.extra_bar).addView(view, 0)
    }

    open fun setLayout() {
        requireView().findViewById<Button>(R.id.bestfit_hierarchy_btn).setOnClickListener {
            changeLayoutView()
        }
        resetBackPress(requireActivity())
        if (bestFit) {
            requireView().findViewById<Button>(R.id.bestfit_hierarchy_btn).text = "Best Fit View"
            requireView().findViewById<TextView>(R.id.change_to).text = "change to folders view"
        } else {
            requireView().findViewById<Button>(R.id.bestfit_hierarchy_btn).text = "Folders View"
            requireView().findViewById<TextView>(R.id.change_to).text = "change to best fit view"
        }
    }

    open fun initFlairs() {

        CoroutineScope(IO).launch {
            val lst =  ArrayList<SpinnerCheckBox>()
            lst.add(SpinnerCheckBox("Questions", PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("question", true), "question"))
            lst.add(SpinnerCheckBox("Suggestions", PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("suggestion", true), "suggestion"))
            lst.add(SpinnerCheckBox("Tests", PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("test", true), "test"))
            lst.add(SpinnerCheckBox("Summaries", PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("summary", true), "summary"))
            lst.add(SpinnerCheckBox("Memes", PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("meme", true), "meme"))
            val ada = MySpinnerAdapter(requireContext(), lst)
            val spinner = requireView().findViewById<Spinner>(R.id.flair_spinner)
            withContext(Main) {
                spinner.adapter = ada
            }
        }
    }

    fun refreshAll() {
        setLayout()
        posts!!.refreshRecyclerView()
        loadPosts()
    }

    fun resetBackPress(activity: ComponentActivity) {
        val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                activity.finish()
            }
        }
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    fun changeLayoutView() {
        bestFit = !bestFit
        val container = requireView().findViewById<LinearLayout>(R.id.posts_container)
        container.removeViews(1, container.childCount - 2)
        refreshAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.saveSuggestions(searchBar!!.getLastSuggestions() as List<String>);
    }
}