package com.darktheme.unitime.viewModels

import android.content.Context
import android.content.Intent
import androidx.databinding.BaseObservable
import com.darktheme.unitime.models.JsonObjects.RegisterObj
import com.darktheme.unitime.models.JsonObjects.RegisterRequest
import com.darktheme.unitime.models.RetrofitClient
import com.darktheme.unitime.views.MainPageActivity
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel(val context : Context) : BaseObservable() {

    fun onRegisterClicked() {
        println("register click")
        RegisterRequest(RetrofitClient.getInstance()!!).post(RegisterObj("a", "b", "c", "d"), onResponse, onFailure)
    }

    val onResponse : (call: Call<RegisterObj>?, response: Response<RegisterObj>?) -> Unit = { call: Call<RegisterObj>?, response: Response<RegisterObj>? ->
        println("ON REGISTER RESPONSE!!!")
        println("Response: " + response!!.message())
        val intent = Intent(context, MainPageActivity::class.java)
        context.startActivity(intent)
    }

    val onFailure : (call: Call<RegisterObj>?, t: Throwable?) -> Unit = { call: Call<RegisterObj>?, t: Throwable? ->
        println("ON REGISTER FAILURE!!!")
        println("Response: " + t!!.message)
    }
}