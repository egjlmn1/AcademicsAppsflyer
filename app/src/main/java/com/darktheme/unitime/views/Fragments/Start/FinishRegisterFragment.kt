package com.darktheme.unitime.views.Fragments.Start

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.ProfileRetrofit
import com.darktheme.unitime.models.Retrofit.JsonObjects.RegisterObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.RegisterRequest
import com.darktheme.unitime.models.Retrofit.RetrofitClient
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
        RegisterRequest(RetrofitClient.getInstance()!!).post(RegisterObj(myActivity!!.email.toLowerCase(), myActivity!!.password1, myActivity!!.name), onRegisterResponse, onRegisterFailure)
        return root
    }

    val onRegisterResponse : (call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>?) -> Unit = { call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>? ->
        if (response!!.code() == 200) {
            holder!!.removeView(loadAnim)
            val finishAnim = LottieAnimationView(requireContext())
            holder!!.addView(finishAnim)
            finishAnim.setAnimation(R.raw.finish)
            finishAnim.playAnimation()
            finishAnim.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    myActivity!!.loginProfile(response.body()!!, R.id.action_finish_register_to_mainPageActivity)
                }
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
        } else {
            if (response.code() == 409) {
                showError("An account with that email already exists")
            } else {
                println("Error code: " + response.code())
                Toast.makeText(requireContext(),"Error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

    val onRegisterFailure : (call: Call<ProfileRetrofit>?, t: Throwable?) -> Unit = { call: Call<ProfileRetrofit>?, t: Throwable? ->
        Toast.makeText(requireContext(),"Error occurred", Toast.LENGTH_SHORT).show();
        println("Error msg: " + t!!.message)
    }

    fun showError(errorMsg: String) {
        val bundle = bundleOf("error" to errorMsg)
        (requireActivity() as StartActivity).navController!!.navigate(R.id.action_finish_register_to_email, bundle)
    }
}