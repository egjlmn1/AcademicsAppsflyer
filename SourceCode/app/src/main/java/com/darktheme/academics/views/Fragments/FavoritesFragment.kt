package com.darktheme.academics.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.darktheme.academics.R
import com.darktheme.academics.databinding.FragmentFavoritesBinding
import com.darktheme.academics.viewModels.FavoritesViewModel
import com.darktheme.academics.views.Activities.MainPageActivity


class FavoritesFragment : Fragment() {

    var viewModel: FavoritesViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFavoritesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
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