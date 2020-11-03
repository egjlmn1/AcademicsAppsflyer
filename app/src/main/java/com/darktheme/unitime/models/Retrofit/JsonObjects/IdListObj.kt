package com.darktheme.unitime.models.Retrofit.JsonObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class SearchResponseListObj(val posts_ids : List<SearchResponse>) {}
class SearchResponse(val view_type : Int, val content : String) {}
class SearchObj(val search: String, val flair: String)

interface IdListAPI {

    @POST("search")
    fun getIdListJson(@Body search : SearchObj) : Call<SearchResponseListObj>
}

class IdListRequest(val retrofit: Retrofit) {

    fun get(search : String, flair : String,
            myOnResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit, myOnFaliure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<SearchResponseListObj> = api.getIdListJson(SearchObj(search, flair))
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