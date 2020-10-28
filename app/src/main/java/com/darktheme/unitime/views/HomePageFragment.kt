package com.darktheme.unitime.views

import PostsAdapter
import ScrollListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.R
import com.darktheme.unitime.models.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.JsonObjects.PostContentObj
import com.darktheme.unitime.models.JsonObjects.PostObj
import com.darktheme.unitime.models.MyViewHolder
import com.darktheme.unitime.viewModels.PostsViewModel


class HomePageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home_page, container, false)
        return root
    }

    companion object {
        val TAG = "HomePageFragmentTAG"
    }
}