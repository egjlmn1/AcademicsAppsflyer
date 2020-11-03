package com.darktheme.unitime.models.Retrofit

import android.app.Activity
import com.darktheme.unitime.models.Retrofit.JsonObjects.*
import retrofit2.Call
import retrofit2.Response

class PostLoader(val activity: Activity, val posts: MutableList<PostObj>, val refreshRV: (Int) -> Unit, val addFolder: (String) -> Unit, val onError: (String) -> Unit) {

    companion object {
        val postViewType = 1
        val hierarchyViewType = 2
    }

    var pos : Int = 0
    var searchResult : List<SearchResponse>? = null

    fun loadIdList(value: String) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).get("search", "new_posts", idResponse, idFailure)
    }

    fun loadPosts() {
        pos = posts.size
        //println("start pos = " + pos)
        for (result in searchResult!!) {//.subList(0, Math.min(10, searchResult!!.size))) {
            if (result.view_type == postViewType) {
                var id = result.content
                println("ID = " + id)
                PostRequest(
                    RetrofitClient.getInstance()!!
                ).get(id, getPostResponse(pos), postFailure)
                pos += 1
            } else if (result.view_type == hierarchyViewType) {
                addFolder(result.content)
            }
        }
    }

    fun startSearch() {
        activity.onSearchRequested()

    }

    private fun insertPost(post : PostObj, position: Int) {
        if (posts.size < position) {
            //println("inserting " + view.post!!.post_id + " at " + posts.size)
            posts.add(post)
            refreshRV(posts.size-1)
        } else {
            //println("inserting " + view.post!!.post_id + " at " + position)
            posts.add(position, post)
            refreshRV(position)
        }
    }

    val idResponse : (call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>?) -> Unit = { call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>? ->
        //println("ON IDLIST RESPONSE!!!")
        //println("Response: " + response!!.message())

        if (response!!.body() != null) {
            searchResult = response.body()!!.posts_ids.toList()
            loadPosts()
        }
    }

    val idFailure : (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit = { call: Call<SearchResponseListObj>?, t: Throwable? ->
        //println("ON IDLIST FAILURE!!!")
        //println("Response: " + t!!.message)
        onError("Academics is under maintenance, come back later")
    }

    fun getPostResponse(postPosition : Int): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            //println("ON POST RESPONSE!!!")
            //println("Response: " + response!!.message())
            val p : PostObj = response!!.body()!!
            insertPost(p, postPosition)
        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        //println("ON POST FAILURE!!!")
        //println("Response: " + t!!.message)
    }
}