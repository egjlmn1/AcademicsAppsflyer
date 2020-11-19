package com.darktheme.unitime.views.Fragments.Start

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.RegisterObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.RegisterRequest
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import com.darktheme.unitime.models.Room.Profile
import com.darktheme.unitime.views.Activities.StartActivity
import retrofit2.Call
import retrofit2.Response


class FinishRegisterFragment : Fragment() {

    var holder: RelativeLayout? = null
    var loadAnim: LottieAnimationView? = null
    var myActivity: StartActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_finish_register, container, false)

        holder = root.findViewById<RelativeLayout>(R.id.animation_holder)

        myActivity = (requireActivity() as StartActivity)

        loadAnim = root.findViewById<LottieAnimationView>(R.id.animationView)
        loadAnim!!.cancelAnimation()
        //TODO replace button click with server response
        RegisterRequest(RetrofitClient.getInstance()!!).post(RegisterObj(myActivity!!.email.toLowerCase(), myActivity!!.name, myActivity!!.password1), onRegisterResponse, onRegisterFailure)
        return root
    }

    val onRegisterResponse : (call: Call<Profile>?, response: Response<Profile>?) -> Unit = { call: Call<Profile>?, response: Response<Profile>? ->
        if (response!!.code() == 200) {
            // TODO add progress bar with text "logging in"
            // TODO send login request to server
            holder!!.removeView(loadAnim)
            val finishAnim = LottieAnimationView(requireContext())
            holder!!.addView(finishAnim)
            finishAnim.setAnimation(R.raw.finish)
            finishAnim.playAnimation()
            finishAnim.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    myActivity!!.loginProfile(response.body()!!, R.id.action_finish_register_to_build_profile)
                }
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        } else {
            showError("An account with that email already exists")
        }
    }

    val onRegisterFailure : (call: Call<Profile>?, t: Throwable?) -> Unit = { call: Call<Profile>?, t: Throwable? ->
        //TODO handle register response
        // something like "An account with that email already exists"
        showError("An account with that email already exists")
        println(t!!.message)
    }

    fun showError(errorMsg: String) {
        val bundle = bundleOf("error" to errorMsg)
        (requireActivity() as StartActivity).navController!!.navigate(R.id.action_finish_register_to_email, bundle)
    }

    val onLoginFailure : (call: Call<Profile>?, t: Throwable?) -> Unit = { call: Call<Profile>?, t: Throwable? ->
        //TODO display failed to login message
        // something like problem with the server, try again
        println(t!!.message)
    }

}