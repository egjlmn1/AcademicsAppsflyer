package com.darktheme.unitime.models.Retrofit

import android.app.Activity
import com.darktheme.unitime.models.Retrofit.JsonObjects.*
import com.darktheme.unitime.models.Room.Profile
import retrofit2.Call
import retrofit2.Response

class PostLoader(val activity: Activity, val posts: MutableList<PostObj>, val refreshRV: (Int) -> Unit, val addFolder: (String) -> Unit, val onError: (String) -> Unit) {

    companion object {
        val postViewType = 1
        val hierarchyViewType = 2
    }

    var pos : Int = 0
    var searchResult : List<SearchResponse>? = null

    fun searchIdList(searchText: String, flair: FlairObj) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).search(searchText, flair, idResponse, idFailure)
    }

    fun foldersIdList(folder: String?) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).folders(folder, idResponse, idFailure)
    }

    fun bestfitIdList(email: String, flair: FlairObj) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).bestfit(email, flair, idResponse, idFailure)
    }

    fun myIdList(email: String) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).myPosts(email, idResponse, idFailure)
    }

    fun loadPosts() {
        pos = posts.size
        //println("start pos = " + pos)
        for (result in searchResult!!) {//.subList(0, Math.min(10, searchResult!!.size))) {
            if (result.view_type == postViewType) {
                val id = result.content
                PostRequest(
                    RetrofitClient.getInstance()!!
                ).get(id, getPostResponse(pos), postFailure)
                pos += 1
            } else if (result.view_type == hierarchyViewType) {
                addFolder(result.content)
            }
        }
    }

    private fun insertPost(post : PostObj, position: Int) {
        if (posts.size < position) {
            posts.add(post)
            refreshRV(posts.size-1)
        } else {
            posts.add(position, post)
            refreshRV(position)
        }
    }

    val idResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit = { call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>? ->
        if (response!!.code() != 200) {
            onError("Academics is under maintenance, come back later")
        } else {
            if (response.body() != null) {
                searchResult = response.body()!!.posts_ids.toList()
                loadPosts()
            }
        }
    }

    val idFailure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit = { call: Call<SearchResponseListObj>?, t: Throwable? ->
        onError("Academics is under maintenance, come back later")
    }

    fun getPostResponse(postPosition : Int): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            println("Response for post: " + postPosition)
            val code = response!!.code()
            if (code == 200) {
                val p : PostObj = response.body()!!
                insertPost(p, postPosition)
            }
        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        //println("ON POST FAILURE!!!")
        //println("Response: " + t!!.message)
    }
}