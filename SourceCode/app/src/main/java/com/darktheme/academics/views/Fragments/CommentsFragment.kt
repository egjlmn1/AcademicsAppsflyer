package com.darktheme.academics.views.Fragments

import CommentsAdapter
import PostView
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.academics.OnFileClickListener
import com.darktheme.academics.R
import com.darktheme.academics.models.Retrofit.JsonObjects.*
import com.darktheme.academics.models.Retrofit.RetrofitClient
import com.darktheme.academics.viewModels.PostsViewModel
import com.darktheme.academics.views.Activities.MainPageActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class CommentsFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var commentsList : MutableList<GetCommentObj> = ArrayList()
    var myContainer : ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myContainer = container
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        root.findViewById<Button>(R.id.send_btn).setOnClickListener {
            sendComment()
        }
        return root
    }

    fun sendComment() {
        val comment = requireView().findViewById<EditText>(R.id.comment_text).text.toString().trim()
        requireView().findViewById<EditText>(R.id.comment_text).text.clear()
        val id = (requireActivity() as MainPageActivity).user_id
        if (comment.isNotEmpty() && id != null) {
            requireView().findViewById<Button>(R.id.send_btn).isEnabled = false
            println("COMMENT: " + comment)

            CommentRequest(RetrofitClient.getInstance()!!).post(arguments?.getString("id")!!, PostCommentObj(id.toString(), comment), onSendResponse, onSendFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
        PostRequest(RetrofitClient.getInstance()!!).get(id!!, getPostResponse(), postFailure)
        initRecyclerView()
        setUpComments()
    }

    fun getPostResponse(): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { _: Call<PostObj>?, response: Response<PostObj>? ->
            val p : PostObj = response!!.body()!!
            val postView = PostView(requireActivity() as MainPageActivity, requireView())
            postView.postInfo!!.setOnClickListener{
                val bundle = bundleOf("userid" to p.publisher_email)
                (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_comments_to_nav_image, bundle)
            }
            postView.setUp(p, OnFileClickListener(requireContext()))
            postView.textContent!!.setTextIsSelectable(true)
            Linkify.addLinks(postView.textContent, Linkify.WEB_URLS);
            postView.textContent.linksClickable = true
            if (p.type == PostsViewModel.ImageType) {
                postView.postImage!!.setOnClickListener{
                    val bundle = bundleOf("id" to p.post_id )
                    (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_comments_to_nav_image, bundle)
                }
            }

        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { _: Call<PostObj>?, t: Throwable? ->
        println("ON POST FAILURE!!!")
        println("Response: " + t!!.message)
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.comments_recycler_view)
        //recyclerView!!.addOnScrollListener(ScrollListener())
        recyclerView!!.adapter = CommentsAdapter(requireActivity(), commentsList)
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
    }

    val onFailure = { _: Call<List<GetCommentObj>>?, t: Throwable? ->
        println("error loading comments")
        println(t!!.message)
    }

    val onResponse = { _: Call<List<GetCommentObj>>?, response: Response<List<GetCommentObj>>? ->
        if (response!!.code() != 200) {
            println("Error loading comments")
        } else {
            println("loaded comments")
            val size = commentsList.size
            commentsList.clear()
            recyclerView!!.adapter!!.notifyItemRangeRemoved(0, size)
            val lst = response.body()
            for (c in lst!!) {
                println(c.content)
            }
            commentsList.addAll(lst!!)
            recyclerView!!.adapter!!.notifyItemRangeInserted(0, commentsList.size)
        }
    }

    val onSendFailure = { _: Call<ResponseBody>?, t: Throwable? ->
        println("failed to send comment")
        println(t!!.message)
        Toast.makeText(requireContext(),"Failed to post comment", Toast.LENGTH_SHORT).show()
        requireView().findViewById<Button>(R.id.send_btn).isEnabled = true
    }

    val onSendResponse = { _: Call<ResponseBody>?, response: Response<ResponseBody>? ->
        requireView().findViewById<Button>(R.id.send_btn).isEnabled = true
        if (response!!.code() != 200) {
            println("Error loading comments")
        } else {
            setUpComments()
        }
    }

    fun setUpComments() {
        CommentRequest(RetrofitClient.getInstance()!!).get(arguments?.getString("id")!!, onResponse, onFailure)
    }

    companion object {
    }
}