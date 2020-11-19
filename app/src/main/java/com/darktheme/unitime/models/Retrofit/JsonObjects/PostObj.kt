package com.darktheme.unitime.models.Retrofit.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class PostObj(val post_id : String, val type : String, val sub_type : String, val date : String, val flair : String?, val publisher : String, val user_email: String, val text_content : String?, val attachment : AttachmentObj?) {}
class AttachmentObj(val name : String, val type : String)
interface PostAPI {

    @GET("post/metadata")
    fun getPostJson(@Query("id") id : String) : Call<PostObj>
}

class PostRequest(val retrofit: Retrofit) {

    fun get(id : String,
        myOnResponse : (call: Call<PostObj>?, response: Response<PostObj>?) -> Unit, myOnFaliure : (call: Call<PostObj>?, t: Throwable?) -> Unit) {
        val api : PostAPI = retrofit.create(PostAPI::class.java)
        val call : Call<PostObj> = api.getPostJson(id)
        call.enqueue(object : Callback<PostObj> {
            override fun onFailure(call: Call<PostObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<PostObj>?, response: Response<PostObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}