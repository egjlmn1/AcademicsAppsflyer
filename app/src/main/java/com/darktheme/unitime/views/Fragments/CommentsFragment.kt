package com.darktheme.unitime.views.Fragments

import CommentsAdapter
import PostView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnCommentClickListener
import com.darktheme.unitime.OnFileClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.CommentObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostRequest
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import retrofit2.Call
import retrofit2.Response


class CommentsFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var commentsList : ArrayList<CommentObj>? = null

    var myContainer : ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myContainer = container
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        val id = arguments?.getString("id")
        PostRequest(RetrofitClient.getInstance()!!).get(id!!, getPostResponse(), postFailure)
    }

    fun getPostResponse(): (Call<PostObj>?, Response<PostObj>?) -> Unit {
        return { call: Call<PostObj>?, response: Response<PostObj>? ->
            println("ON POST RESPONSE!!!")
            //println("Response: " + response!!.message())
            val p : PostObj = response!!.body()!!
            val postView = PostView(requireContext(), requireView())
            postView.postInfo.setOnClickListener{
                val bundle = bundleOf("userid" to p.user_email)
                (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_comments_to_nav_image, bundle)
            }
            postView.setUp(p, OnFileClickListener(requireContext()))
            if (p.type == PostsViewModel.ImageType) {
                postView.postImage!!.setOnClickListener{
                    val bundle = bundleOf("id" to p.post_id )
                    (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_comments_to_nav_image, bundle)
                }
            }

        }
    }

    val postFailure : (call: Call<PostObj>?, t: Throwable?) -> Unit = { call: Call<PostObj>?, t: Throwable? ->
        println("ON POST FAILURE!!!")
        println("Response: " + t!!.message)
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.comments_recycler_view)
        //recyclerView!!.addOnScrollListener(ScrollListener())

        commentsList = ArrayList()
        tempListFill()
        // TODO:
        // get comments from server
        // move it all to the view model
        // create view model...
        recyclerView!!.adapter = CommentsAdapter(requireActivity(), commentsList!!, OnCommentClickListener((requireActivity() as MainPageActivity).navController!!))
        recyclerView!!.layoutManager = LinearLayoutManager(requireActivity())
    }

    fun tempListFill() {
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
    }

    fun setUpComments(scrollView : LinearLayout) {

    }

    companion object {
        val TAG = "CommentsFragmentTAG"
    }
}