package com.darktheme.unitime.models.Retrofit

import android.app.Activity
import com.darktheme.unitime.models.Retrofit.JsonObjects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class PostLoader(val activity: Activity, val posts: MutableList<PostObj>, val refreshRV: (Int) -> Unit, val addFolder: (String) -> Unit, val onError: (String) -> Unit) {

    companion object {
        val postViewType = 1
        val hierarchyViewType = 2
    }

    var pos : Int = 0

    fun searchIdList(searchText: String, flair: FlairObj, onResult: () -> Unit) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).search(searchText, flair, getIdResponse(onResult), getIdFailure(onResult))
    }

    fun foldersIdList(folder: String, flair: FlairObj, onResult: () -> Unit) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).folders(folder, flair, getIdResponse(onResult), getIdFailure(onResult))
    }

    fun bestfitIdList(email: String, flair: FlairObj, onResult: () -> Unit) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).bestfit(email, flair, getIdResponse(onResult), getIdFailure(onResult))
    }

    fun myIdList(email: String, onResult: () -> Unit) {
        IdListRequest(
            RetrofitClient.getInstance()!!
        ).myPosts(email, getIdResponse(onResult), getIdFailure(onResult))
    }

    fun loadPosts(searchResult : List<SearchResponse>) {
        if (posts.isNotEmpty()) {
            println("POSTS NOT EMPTY")
        }
        posts.clear()
        pos = posts.size
        posts.clear()
        //println("start pos = " + pos)
        for (result in searchResult) {//.subList(0, Math.min(10, searchResult!!.size))) {
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


    fun getIdResponse(onResult: () -> Unit): (Call<SearchResponseListObj>?, Response<SearchResponseListObj>?) -> Unit {
        return { call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>? ->
            println("SUCCESS")
            if (response!!.code() != 200) {
                println(response.code())
                onError("Academics is under maintenance, come back later")
            } else {
                if (response.body() != null) {
                    val searchResult = response.body()!!.posts_ids.toList()
                    loadPosts(searchResult)
                }
            }
            onResult()
        }
    }

    fun getIdFailure(onResult: () -> Unit): (call: Call<SearchResponseListObj>?, t: Throwable?) -> Unit {
        return { call: Call<SearchResponseListObj>?, t: Throwable? ->
            println("ERROR PostLoader")
            onError("Academics is under maintenance, come back later")
            onResult()
        }
    }


    fun getPostResponse(postPosition : Int): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            val code = response!!.code()

            if (code != 200) {
                //println(response.code())
            } else {
                val p: PostObj = response.body()!!
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        insertPost(p, postPosition)
                    }
                }

            }
        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        //println("ON POST FAILURE!!!")
        //println("Response: " + t!!.message)
    }
}