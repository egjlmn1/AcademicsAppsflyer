package com.darktheme.academics.views.Fragments.Start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.darktheme.academics.R
import com.darktheme.academics.views.Activities.StartActivity


class StartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_start, container, false)
        val activity = requireActivity() as StartActivity
        activity.email = ""
        activity.password1 = ""
        activity.password2 = ""
        activity.name = ""

        root.findViewById<Button>(R.id.register_btn).setOnClickListener {
            activity.navController!!.navigate(R.id.action_nav_start_to_email)
        }
        root.findViewById<Button>(R.id.login_btn).setOnClickListener {
            activity.navController!!.navigate(R.id.action_nav_start_to_login)
        }
        return root
    }
}