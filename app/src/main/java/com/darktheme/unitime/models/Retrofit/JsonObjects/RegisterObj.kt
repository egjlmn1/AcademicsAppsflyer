package com.darktheme.unitime.models.Retrofit.JsonObjects

import com.darktheme.unitime.models.Room.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class RegisterObj(val email : String, val name : String, val password : String) {}

interface RegisterAPI {

    @POST("register")
    fun postRegisterJson(@Body register : RegisterObj) : Call<Profile>
}

class RegisterRequest(val retrofit: Retrofit) {

    fun post(register : RegisterObj,
        myOnResponse : (call: Call<Profile>?, response: Response<Profile>?) -> Unit, myOnFaliure : (call: Call<Profile>?, t: Throwable?) -> Unit) {
        val api : RegisterAPI = retrofit.create(RegisterAPI::class.java)
        val call : Call<Profile> = api.postRegisterJson(register)
        call.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<Profile>?, response: Response<Profile>?) {
                myOnResponse(call, response)
            }

        })
    }
}