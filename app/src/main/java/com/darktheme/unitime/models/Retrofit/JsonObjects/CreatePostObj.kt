package com.darktheme.unitime.models.Retrofit.JsonObjects

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class CreatePostObj (val metadata: PostObj, val attachment: String?) {}
interface CreatePostAPI {

    @POST("post")
    fun postPostJson(@Body post: CreatePostObj) : Call<ResponseBody>
}

class CreatePostRequest(val retrofit: Retrofit) {

    fun post(post : CreatePostObj,
        myOnResponse : (call: Call<ResponseBody>?, response: Response<ResponseBody>?) -> Unit, myOnFaliure : (call: Call<ResponseBody>?, t: Throwable?) -> Unit) {
        val api : CreatePostAPI = retrofit.create(CreatePostAPI::class.java)
        val call : Call<ResponseBody> = api.postPostJson(post)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                myOnResponse(call, response)
            }

        })
    }
}