package com.darktheme.unitime

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.darktheme.unitime.models.Retrofit.DownloadFileRequest
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

interface OnItemClickListener {
    fun onItemClicked(data: String)
}

class OnPostClickListener(val navController: NavController) : OnItemClickListener {
    override fun onItemClicked(data: String) {
        val post = Gson().fromJson(data, PostObj::class.java)
        println("ID = " + post.post_id)
        val bundle = bundleOf("id" to post.post_id)
        navController.navigate(R.id.action_nav_posts_to_nav_full_post, bundle) // with parameters
    }
}

class OnCommentClickListener(val navController: NavController) : OnItemClickListener {
    override fun onItemClicked(data: String) {

    }
}

class OnFileClickListener(val contex: Context) : OnItemClickListener {
    override fun onItemClicked(data: String) {
        val onResponse = getFileResponse(data)
        DownloadFileRequest(
            RetrofitClient.getInstance()!!
        ).get("search", onResponse, onFailure)
    }

    val onFailure : (call: Call<ResponseBody>?, t: Throwable?) -> Unit = { call: Call<ResponseBody>?, t: Throwable? ->
        println("ON FILE FAILURE!!!")
        println("Response: " + t!!.message)
    }

    val getFileResponse: (String)->((Call<ResponseBody>?, Response<ResponseBody>?) -> Unit) = {id: String ->
        { call: Call<ResponseBody>?, response: Response<ResponseBody>? ->
            //println("ON POST RESPONSE!!!")
            //println("Response: " + response!!.message())
            try{
                println("downloading file")
                val bytes = response!!.body()!!.bytes()
                val file = File(StringBuilder(id).append(".pdf").toString())
                val fos = FileOutputStream(file)
                fos.write(bytes)
                fos.flush();
                fos.close();
            } catch (e: Exception) {
                val toast = Toast(contex)
                toast.setText("could not download file")
                toast.show()
            }

        }
    }
}