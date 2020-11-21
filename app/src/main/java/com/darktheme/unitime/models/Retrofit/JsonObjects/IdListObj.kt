package com.darktheme.unitime.models.Retrofit.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class SearchResponseListObj(val posts_ids: List<SearchResponse>) {}
class SearchResponse(val view_type: Int, val content: String) {}
class SearchObj(val search: String, val flair: FlairObj) {}
class FlairObj(val question: Boolean, val suggestion: Boolean, val test: Boolean, val summary: Boolean, val meme: Boolean)

interface IdListAPI {

    @POST("search")
    fun searchIdListJson(@Body search :SearchObj) : Call<SearchResponseListObj>
    @GET("Hierarchy")
    fun  foldersIdListJson(@Query("path") path : String) : Call<SearchResponseListObj>
    //@POST("bestfit")
    //fun bestfitIdListJson(@Body email :SearchObj) : Call<SearchResponseListObj>
    @GET("search")
    fun myPostsIdListJson(@Query("email") email: String) : Call<SearchResponseListObj>
}

class IdListRequest(val retrofit: Retrofit) {

    fun search(search : String, flair: FlairObj,
               myOnResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit, myOnFaliure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<SearchResponseListObj> = api.searchIdListJson(SearchObj(search, flair))
        call.enqueue(object : Callback<SearchResponseListObj> {
            override fun onFailure(call: Call<SearchResponseListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) {
                myOnResponse(call, response)
            }

        })
    }

    fun folders(folder : String,
               myOnResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit, myOnFaliure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<SearchResponseListObj> = api.foldersIdListJson(folder)
        call.enqueue(object : Callback<SearchResponseListObj> {
            override fun onFailure(call: Call<SearchResponseListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) {
                myOnResponse(call, response)
            }

        })
    }

    fun bestfit(email: String, flair: FlairObj,
               myOnResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit, myOnFaliure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<SearchResponseListObj> = api.searchIdListJson(SearchObj("", flair))
        call.enqueue(object : Callback<SearchResponseListObj> {
            override fun onFailure(call: Call<SearchResponseListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) {
                myOnResponse(call, response)
            }

        })
    }

    fun myPosts(email: String,
                myOnResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit, myOnFaliure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<SearchResponseListObj> = api.myPostsIdListJson(email)
        call.enqueue(object : Callback<SearchResponseListObj> {
            override fun onFailure(call: Call<SearchResponseListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}