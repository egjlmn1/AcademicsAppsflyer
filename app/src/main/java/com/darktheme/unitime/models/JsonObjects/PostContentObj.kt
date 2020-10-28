package com.darktheme.unitime.models.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

class PostContentObj(val PostContent_id : String, val type : String, val date : String, val flair : String, val publisher : String) {}

interface PostContentAPI {

    @GET("postContent")
    fun getPostContentJson(id : String) : Call<PostContentObj>
}

class PostContentRequest(val retrofit: Retrofit) {

    fun get(id : String,
        myOnResponse : (call: Call<PostContentObj>?, response: Response<PostContentObj>?) -> Unit, myOnFaliure : (call: Call<PostContentObj>?, t: Throwable?) -> Unit) {
        val api : PostContentAPI = retrofit.create(PostContentAPI::class.java)
        val call : Call<PostContentObj> = api.getPostContentJson(id)
        call.enqueue(object : Callback<PostContentObj> {
            override fun onFailure(call: Call<PostContentObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<PostContentObj>?, response: Response<PostContentObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}