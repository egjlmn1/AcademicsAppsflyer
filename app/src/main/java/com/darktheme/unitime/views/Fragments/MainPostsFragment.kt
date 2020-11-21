package com.darktheme.unitime.views.Fragments

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


open class MainPostsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var posts : PostsLayout? = null
    var containerView: ViewGroup? = null
    var viewModel : PostsViewModel? = null

    var searchBar: MaterialSearchBar? = null
    var swipeContainer: SwipeRefreshLayout? = null

    var bestFit: Boolean = false

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
    }

    open fun loadPosts() {
        swipeContainer!!.setRefreshing(true);
        viewModel!!.loadPosts(bestFit, (requireActivity() as MainPageActivity).currentPath){ swipeContainer!!.setRefreshing(false);}
    }

    fun initRefresh() {
        swipeContainer = requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        swipeContainer!!.setOnRefreshListener(this)

        swipeContainer!!.setColorSchemeResources(R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark);
        swipeContainer!!.post(Runnable {
            swipeContainer!!.setRefreshing(true)

            // Fetching data from server
            loadPosts()
        })
    }

    fun initSearchBar() {
        searchBar = requireView().findViewById<MaterialSearchBar>(R.id.searchBar)
        searchBar!!.setOnSearchActionListener(viewModel)
        //viewModel!!.loadSuggestions(searchBar!!)
    }

    open fun initLayout() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_change_view, null, false)
        requireView().findViewById<RelativeLayout>(R.id.extra_container).addView(view, 0)
    }

    open fun setLayout() {
        requireView().findViewById<Button>(R.id.bestfit_hierarchy_btn).setOnClickListener {
            changeLayoutView()
        }
        val layout =  requireView().findViewById<LinearLayout>(R.id.posts_container)
        if (layout.childCount > 2) {
            layout.removeViewAt(1)
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
                if ((activity as MainPageActivity).currentPath.isNotEmpty()) {
                    val splitedPath = activity.currentPath.split('/')
                    if (splitedPath.size == 1) {
                        activity.currentPath = ""
                    } else {
                        val newPath = splitedPath.subList(0, splitedPath.size-1).joinToString(separator = "/")
                        activity.currentPath = newPath
                    }
                    refreshAll()
                } else {
                    activity.finish()
                }
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

    override fun onRefresh() {
        refreshAll()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        viewModel!!.saveSuggestions(searchBar!!.getLastSuggestions() as List<String>);
//    }
}