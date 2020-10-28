package com.darktheme.unitime.views

import PostView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.JsonObjects.*
import com.darktheme.unitime.models.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class CommentsFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var commentsList : ArrayList<CommentObj>? = null

    var myContainer : ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myContainer = container
        val root = inflater.inflate(R.layout.fragment_full_post, container, false)
        return root
    }

    override fun onStart() {
        super.onStart()
        initScrollView()
        PostRequest(RetrofitClient.getInstance()!!).get(requireArguments().getString("id")!!, getPostResponse(), postFailure)
    }

    fun getPostResponse(): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            println("ON POST RESPONSE!!!")
            //println("Response: " + response!!.message())
            val p : PostObj = response!!.body()!!
            PostView(requireContext(), requireView()).setUp(p)
        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        println("ON POST FAILURE!!!")
        println("Response: " + t!!.message)
    }

    private fun initScrollView() {
        commentsList = ArrayList()
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        commentsList!!.add(CommentObj("Yoav Naftali", "3m", "some comment"))
        commentsList!!.add(CommentObj("Shachar Meir", "2h", "another comment"))
        // TODO:
        // get comments from server
        // move it all to the view model
        // create view model...
        val scrollView = requireView().findViewById<LinearLayout>(R.id.comments_scroll)
        setUpComments(scrollView)
    }

    fun setUpComments(scrollView : LinearLayout) {
        for (comment in commentsList!!) {
            var view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_post_comment, myContainer, false)
            view.findViewById<TextView>(R.id.comment_content).text = comment.content
            view.findViewById<TextView>(R.id.comment_publisher).text = comment.publisher
            view.findViewById<TextView>(R.id.comment_date).text = comment.date
            scrollView.addView(view)
        }
    }

    companion object {
        val TAG = "CommentsFragmentTAG"
    }
}