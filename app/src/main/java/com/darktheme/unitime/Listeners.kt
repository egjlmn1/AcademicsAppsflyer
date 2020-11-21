package com.darktheme.unitime

import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.darktheme.unitime.models.Retrofit.DownloadTask
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.google.gson.Gson

interface OnItemClickListener {
    fun onItemClicked(data: String)
}

class OnPostClickListener(val navController: NavController) : OnItemClickListener {
    override fun onItemClicked(data: String) {
        val post = Gson().fromJson(data, PostObj::class.java)
        println("ID = " + post.post_id)
        val bundle = bundleOf("id" to post.post_id)
        navController.navigate(R.id.action_to_nav_comments, bundle) // with parameters
    }
}

class OnCommentClickListener(val navController: NavController) : OnItemClickListener {
    override fun onItemClicked(data: String) {

    }
}

class OnFileClickListener(val contex: Context) : OnItemClickListener {
    override fun onItemClicked(data: String) {
        val id = data.split(' ')[0]
        val filename = data.substring(id.length+1)
        try{
            DownloadTask(contex as MainPageActivity, StringBuilder(AppInfo.serverUrl).append("/post/content?id=").append(id).toString(), filename)
        } catch (e: Exception) {
            Toast.makeText(contex,"Failed to download",Toast.LENGTH_SHORT).show()
            println(e.message)
        }
    }


}