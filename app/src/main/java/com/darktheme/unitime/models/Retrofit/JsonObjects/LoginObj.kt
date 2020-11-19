package com.darktheme.unitime.models.Retrofit.JsonObjects

import com.darktheme.unitime.models.Room.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginAPI {

    @GET("login")
    fun postLoginJson(@Query("email") email : String, @Query("password")password: String) : Call<Profile>
}

class LoginRequest(val retrofit: Retrofit) {

    fun post(email : String, password: String,
        myOnResponse : (call: Call<Profile>?, response: Response<Profile>?) -> Unit, myOnFaliure : (call: Call<Profile>?, t: Throwable?) -> Unit) {
        val api : LoginAPI = retrofit.create(LoginAPI::class.java)
        val call : Call<Profile> = api.postLoginJson(email, password)
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