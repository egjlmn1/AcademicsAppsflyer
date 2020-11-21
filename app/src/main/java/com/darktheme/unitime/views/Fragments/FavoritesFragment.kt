package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentFavoritesBinding
import com.darktheme.unitime.viewModels.FavoritesViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity


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