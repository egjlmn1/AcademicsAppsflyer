package com.darktheme.unitime.models.Retrofit.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class RegisterObj(val email : String, val password: String, val name : String) {}

interface RegisterAPI {

    @POST("register")
    fun postRegisterJson(@Body register : RegisterObj) : Call<ProfileRetrofit>
}

class RegisterRequest(val retrofit: Retrofit) {

    fun post(register : RegisterObj,
        myOnResponse : (call: Call<ProfileRetrofit>?, response: Response<ProfileRetrofit>?) -> Unit, myOnFaliure : (call: Call<ProfileRetrofit>?, t: Throwable?) -> Unit) {
        val api : RegisterAPI = retrofit.create(RegisterAPI::class.java)
        val call : Call<ProfileRetrofit> = api.postRegisterJson(register)
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