package com.darktheme.unitime.models.Retrofit

import com.darktheme.unitime.models.Retrofit.JsonObjects.CommentListAPI
import com.darktheme.unitime.models.Retrofit.JsonObjects.CommentListObj
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface DownloadFileAPI {
    @GET("post/content")
    fun downloadFile(@Query("id") id : String): Call<ResponseBody>
}


class DownloadFileRequest(val retrofit: Retrofit) {

    fun get(id : String, myOnResponse : (call: Call<ResponseBody>?, response: Response<ResponseBody>?) -> Unit, myOnFaliure : (call: Call<ResponseBody>?, t: Throwable?) -> Unit) {
        val api : DownloadFileAPI = retrofit.create(DownloadFileAPI::class.java)
        val call : Call<ResponseBody> = api.downloadFile(id)
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