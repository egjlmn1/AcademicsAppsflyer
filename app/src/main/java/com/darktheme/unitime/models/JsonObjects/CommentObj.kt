package com.darktheme.unitime.models.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class CommentObj(val publisher : String, val date: String, val content: String) {}
class CommentListObj(val post_comments : List<CommentObj>) {}

interface CommentListAPI {

    @GET("comments")
    fun getCommentListJson(@Query("id") id : String) : Call<CommentListObj>
}

class CommentListRequest(val retrofit: Retrofit) {

    fun get(id : String,
            myOnResponse : (call: Call<CommentListObj>?, response: Response<CommentListObj>?) -> Unit, myOnFaliure : (call: Call<CommentListObj>?, t: Throwable?) -> Unit) {
        val api : CommentListAPI = retrofit.create(CommentListAPI::class.java)
        val call : Call<CommentListObj> = api.getCommentListJson(id)
        call.enqueue(object : Callback<CommentListObj> {
            override fun onFailure(call: Call<CommentListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<CommentListObj>?, response: Response<CommentListObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}