package com.darktheme.unitime.views

import PostsAdapter
import ScrollListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.JsonObjects.PostContentObj
import com.darktheme.unitime.models.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel


class CreatePostFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var postObjList : ArrayList<PostObj>? = null
    var postContentList : ArrayList<PostContentObj>? = null

    var viewModel : PostsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)
        println("create post created")
        return view
    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        val TAG : String = "CreatePostFragmentTAG"
    }
}