package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darktheme.unitime.R
import com.darktheme.unitime.databinding.FragmentPostsBinding
import com.darktheme.unitime.databinding.FragmentProfileBinding
import com.darktheme.unitime.models.Room.AppDataBase
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.viewModels.ProfileViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.darktheme.unitime.views.CustomViews.PostsLayout
import com.darktheme.unitime.views.CustomViews.ProfileLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    var posts : ProfileLayout? = null
    var viewModel : ProfileViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
        val view: View = binding.getRoot()
        posts = ProfileLayout(view, requireActivity() as MainPageActivity)
        viewModel = posts!!.createViewModel(requireActivity() as MainPageActivity) as ProfileViewModel
        binding.viewModel = viewModel as ProfileViewModel
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
    }

    fun initAll() {
        initProfileData()
        posts!!.initRecyclerView(requireActivity())
        viewModel!!.loadPosts()
    }

    fun initProfileData() {
        val profileName = requireView().findViewById<TextView>(R.id.profile_name)
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDataBase.getInstance(requireContext())
            val email = (requireActivity() as MainPageActivity).email
            if (email != null) {
                val profile = db.profileDao().getProfile(email)
                if (profile != null) {
                    withContext(Dispatchers.Main) {
                        profileName.text = profile.name
                    }
                }
            }

        }
    }
}