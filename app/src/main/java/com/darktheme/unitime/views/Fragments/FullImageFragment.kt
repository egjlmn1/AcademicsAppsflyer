package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.darktheme.unitime.AppInfo
import com.darktheme.unitime.R
import com.github.chrisbanes.photoview.PhotoView


class FullImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_full_image, container, false)
        val photoView = root.findViewById(R.id.full_image) as PhotoView
        val id = arguments?.getString("id")
        Glide.with(this).load(StringBuilder(AppInfo.serverUrl).append("/post/content?id=").append(id).toString()).into(photoView)
        return root
    }
}