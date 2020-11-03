package com.darktheme.unitime.views.Fragments

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.darktheme.unitime.R
import com.darktheme.unitime.views.Activities.StartActivity


class BuildProfileFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_build_profile, container, false)

        val activity = requireActivity() as StartActivity

        root.findViewById<Button>(R.id.build_btn).setOnClickListener {

        }
        root.findViewById<Button>(R.id.skip_btn).setOnClickListener {
            activity.navController!!.navigate(R.id.action_build_profile_to_mainPageActivity)
        }

        return root
    }

}