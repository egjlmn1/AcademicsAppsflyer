package com.darktheme.unitime.models.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class RegisterObj(val umail : String, val email : String, val name : String, val password : String) {}

interface RegisterAPI {

    @POST("register")
    fun postRegisterJson(@Body register : RegisterObj) : Call<RegisterObj>
}

class RegisterRequest(val retrofit: Retrofit) {

    fun post(register : RegisterObj,
        myOnResponse : (call: Call<RegisterObj>?, response: Response<RegisterObj>?) -> Unit, myOnFaliure : (call: Call<RegisterObj>?, t: Throwable?) -> Unit) {
        val api : RegisterAPI = retrofit.create(RegisterAPI::class.java)
        val call : Call<RegisterObj> = api.postRegisterJson(register)
        call.enqueue(object : Callback<RegisterObj> {
            override fun onFailure(call: Call<RegisterObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<RegisterObj>?, response: Response<RegisterObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}