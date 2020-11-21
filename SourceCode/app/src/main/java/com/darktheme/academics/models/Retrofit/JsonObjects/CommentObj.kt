package com.darktheme.academics.models.Retrofit.JsonObjects

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class GetCommentObj(val date: String, val publisher : String, val content: String) {}
class PostCommentObj(val publisher : String, val content: String) {}

interface CommentListAPI {

    @GET("post/comment")
    fun getCommentListJson(@Query("id") id : String) : Call<List<GetCommentObj>>

    @POST("post/comment")
    fun postCommentListJson(@Query("id") id : String, @Body comment: PostCommentObj) : Call<ResponseBody>
}

class CommentRequest(val retrofit: Retrofit) {

    fun get(id : String,
            myOnResponse : (call: Call<List<GetCommentObj>>?, response: Response<List<GetCommentObj>>?) -> Unit, myOnFailure : (call: Call<List<GetCommentObj>>?, t: Throwable?) -> Unit) {
        val api : CommentListAPI = retrofit.create(CommentListAPI::class.java)
        val call : Call<List<GetCommentObj>> = api.getCommentListJson(id)
        call.enqueue(object : Callback<List<GetCommentObj>> {
            override fun onFailure(call: Call<List<GetCommentObj>>?, t: Throwable?) {
                myOnFailure(call, t)
            }

            override fun onResponse(call: Call<List<GetCommentObj>>?, response: Response<List<GetCommentObj>>?) {
                myOnResponse(call, response)
            }

        })
    }

    fun post(id : String, post: PostCommentObj,
            myOnResponse : (call: Call<ResponseBody>?, response: Response<ResponseBody>?) -> Unit, myOnFailure : (call: Call<ResponseBody>?, t: Throwable?) -> Unit) {
        val api : CommentListAPI = retrofit.create(CommentListAPI::class.java)
        val call : Call<ResponseBody> = api.postCommentListJson(id, post)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                myOnFailure(call, t)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                myOnResponse(call, response)
            }

        })
    }
}