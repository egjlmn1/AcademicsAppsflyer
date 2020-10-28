package com.darktheme.unitime.models

import com.darktheme.unitime.models.JsonObjects.PostObj

class MyViewHolder {
    var viewType : Int = -1

    var post : PostObj? = null
    constructor(p : PostObj) {
        viewType = PostsAdapter.postViewType
        post = p
    }

    var dir : String? = null
    constructor(d : String) {
        viewType = PostsAdapter.hierarchyViewType
        dir = d
    }

    constructor(txt1 : String, txt2 : String) {
        viewType = PostsAdapter.blocktViewType
    }
}