package com.darktheme.unitime.models.JsonObjects

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class IdListObj(val posts_ids : List<String>) {}
class SearchObj(val search: String, val flair: String)

interface IdListAPI {

    @POST("search")
    fun getIdListJson(@Body search : SearchObj) : Call<IdListObj>
}

class IdListRequest(val retrofit: Retrofit) {

    fun get(search : String, flair : String,
            myOnResponse : (call: Call<IdListObj>?, response: Response<IdListObj>?) -> Unit, myOnFaliure : (call: Call<IdListObj>?, t: Throwable?) -> Unit) {
        val api : IdListAPI = retrofit.create(IdListAPI::class.java)
        val call : Call<IdListObj> = api.getIdListJson(SearchObj(search, flair))
        call.enqueue(object : Callback<IdListObj> {
            override fun onFailure(call: Call<IdListObj>?, t: Throwable?) {
                myOnFaliure(call, t)
            }

            override fun onResponse(call: Call<IdListObj>?, response: Response<IdListObj>?) {
                myOnResponse(call, response)
            }

        })
    }
}