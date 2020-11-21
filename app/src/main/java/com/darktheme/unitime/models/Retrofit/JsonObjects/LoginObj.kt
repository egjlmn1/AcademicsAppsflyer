package com.darktheme.unitime.models.Retrofit.JsonObjects

import com.darktheme.unitime.models.Room.ProfileDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class ProfileRetrofit(var user_id : String, var email : String, var password : String, var name : String = "") {
    fun toDBProfile(): ProfileDB {
        return ProfileDB(user_id.toInt(), email, password, name)
    }
}

interface LoginAPI {

    @GET("login")
    fun postLoginJson(@Query("email") email : String, @Query("password")password: String) : Call<ProfileRetrofit>
}

class LoginRequest(val retrofit: Retrofit) {

    fun post(email : String, password: String,
        myOnResponse : (call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>?) -> Unit, myOnFaliure : (call: Call<ProfileRetrofit>?, t: Throwable?) -> Unit) {
        val api : LoginAPI = retrofit.create(LoginAPI::class.java)
        val call : Call<ProfileRetrofit> = api.postLoginJson(email, password)
        call.enqueue(object : Callback<ProfileRetrofit> {
            override fun onFailure(call: Call<ProfileRetrofit>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>?) {
                myOnResponse(call, response)
            }

        })
    }
}