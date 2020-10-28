package com.darktheme.unitime.viewModels

import android.app.Activity
import android.content.Context
import android.service.voice.VoiceInteractionSession
import androidx.databinding.BaseObservable
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.models.JsonObjects.*
import com.darktheme.unitime.models.MyViewHolder
import com.darktheme.unitime.models.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class PostsViewModel(val activity: Activity, val recyclerView: RecyclerView, val posts : MutableList<MyViewHolder>, val postsContent : MutableList<PostContentObj>, val viewTypes : ArrayList<Int>) : BaseObservable() {

    companion object {
        val TextType = "text"
        val ImageType = "image"
        val FileType = "file"
    }

    var pos : Int = 0
    var postsID : List<String>? = null

    fun loadIdList() {
        IdListRequest(RetrofitClient.getInstance()!!).get("search", "flair", idResponse, idFailure)
    }

    fun loadFirstTen() {
        pos = posts.size
        println("start pos = " + pos)
        for (id in postsID!!.subList(0, Math.min(10, postsID!!.size))) {
            PostRequest(RetrofitClient.getInstance()!!).get(id, getPostResponse(pos), postFailure)
            pos += 1
        }
    }

    private fun refreshRV(position : Int) {
        recyclerView.adapter!!.notifyItemInserted(position)

        recyclerView.adapter!!.notifyItemRangeChanged(position, (posts.size - 1) - position)

        //recyclerView.smoothScrollToPosition(posts.size)
    }

    fun startSearch() {
        activity.onSearchRequested()

    }

    private fun insertView(view : MyViewHolder, position: Int) {
        if (posts.size < position) {
            posts.add(view)
            viewTypes.add(view.viewType)
            refreshRV(posts.size-1)
        } else {
            posts.add(position, view)
            viewTypes.add(position, view.viewType)
            refreshRV(position)
        }
    }

    val idResponse : (call: Call<IdListObj>?, response: Response<IdListObj>?) -> Unit = { call: Call<IdListObj>?, response: Response<IdListObj>? ->
        println("ON IDLIST RESPONSE!!!")
        //println("Response: " + response!!.message())

        if (response!!.body() != null) {
            postsID = response.body()!!.posts_ids.toList()
            loadFirstTen()
        }
    }

    val idFailure : (call: Call<IdListObj>?, t: Throwable?) -> Unit = { call: Call<IdListObj>?, t: Throwable? ->
        println("ON IDLIST FAILURE!!!")
        println("Response: " + t!!.message)
    }

    fun getPostResponse(postPosition : Int): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            println("ON POST RESPONSE!!!")
            //println("Response: " + response!!.message())
            val p : PostObj = response!!.body()!!
            // get post content for this post
            if (p.type.equals(ImageType)) {
                // load content
            }
            insertView(MyViewHolder(p), postPosition)
        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        println("ON POST FAILURE!!!")
        println("Response: " + t!!.message)
    }

}