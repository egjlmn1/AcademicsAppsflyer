package com.darktheme.unitime.models.Retrofit.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class LoginObj(val email : String, val password: String) {}

interface LoginAPI {

    @POST("login")
    fun postLoginJson(@Body login : LoginObj) : Call<LoginObj>
}

class LoginRequest(val retrofit: Retrofit) {

    fun post(login : LoginObj,
        myOnResponse : (call: Call<LoginObj>?, response: Response<LoginObj>?) -> Unit, myOnFaliure : (call: Call<LoginObj>?, t: Throwable?) -> Unit) {
        val api : LoginAPI = retrofit.create(LoginAPI::class.java)
        val call : Call<LoginObj> = api.postLoginJson(login)
        call.enqueue(object : Callback<LoginObj> {
            override fun onFailure(call: Call<LoginObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<LoginObj>?, response: Response<LoginObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}