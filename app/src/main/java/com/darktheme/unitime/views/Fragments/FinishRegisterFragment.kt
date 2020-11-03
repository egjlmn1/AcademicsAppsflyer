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


class FinishRegisterFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_finish_register, container, false)

        val holder = root.findViewById<RelativeLayout>(R.id.animation_holder)
        //println(arguments?.getString("email"))
        //println(arguments?.getString("password"))
        //println(arguments?.getString("name"))

        val anim = root.findViewById<LottieAnimationView>(R.id.animationView)
        anim.cancelAnimation()
        //TODO replace button click with server response
        root.findViewById<Button>(R.id.continue_btn).setOnClickListener {
            val success = true
            if (success) {
                holder.removeView(anim)
                val anim2 = LottieAnimationView(requireContext())
                holder.addView(anim2)
                anim2.setAnimation(R.raw.finish)
                anim2.playAnimation()
                anim2.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        (requireActivity() as StartActivity).navController!!.navigate(R.id.action_finish_register_to_build_profile)
                    }
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
            } else {
                //TODO get error message from the server
                val errorMsg = "An account with that email already exists"
                val bundle = bundleOf("error" to errorMsg)
                (requireActivity() as StartActivity).navController!!.navigate(R.id.action_finish_register_to_email, bundle)
            }

        }
        return root
    }

}