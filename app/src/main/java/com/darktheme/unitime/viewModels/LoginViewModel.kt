package com.darktheme.unitime.viewModels

import android.content.Context
import android.content.Intent
import androidx.databinding.BaseObservable
import com.darktheme.unitime.models.Retrofit.JsonObjects.LoginObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.LoginRequest
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import com.darktheme.unitime.views.Activities.MainPageActivity
import retrofit2.Call
import retrofit2.Response


class LoginViewModel(val context : Context) : BaseObservable() {

    fun onLoginClicked() {
        println("login click")
        LoginRequest(RetrofitClient.getInstance()!!).post(LoginObj("email", "password"), onResponse, onFailure)
    }

    val onResponse : (call: Call<LoginObj>?, response: Response<LoginObj>?) -> Unit = { call: Call<LoginObj>?, response: Response<LoginObj>? ->
        println("ON LOGIN RESPONSE!!!")
        println("Response: " + response!!.message())
        val intent = Intent(context, MainPageActivity::class.java)
        context.startActivity(intent)
    }

    val onFailure : (call: Call<LoginObj>?, t: Throwable?) -> Unit = { call: Call<LoginObj>?, t: Throwable? ->
        println("ON LOGIN FAILURE!!!")
        println("Response: " + t!!.message)
    }
}