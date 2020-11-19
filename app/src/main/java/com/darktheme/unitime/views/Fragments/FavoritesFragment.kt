package com.darktheme.unitime.views.Fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentFavoritesBinding
import com.darktheme.unitime.databinding.FragmentPostsBinding
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.viewModels.FavoritesViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.CustomViews.FolderView
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesFragment : Fragment() {

    var viewModel: FavoritesViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFavoritesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )
        val root: View = binding.getRoot()
        viewModel = FavoritesViewModel(requireActivity())
        binding.viewModel = viewModel
        initAll(root)
        return root
    }

    fun initAll(view: View) {
        viewModel!!.initSearchBar(view)
        viewModel!!.loadFolders(view, requireActivity() as MainPageActivity)
    }

}