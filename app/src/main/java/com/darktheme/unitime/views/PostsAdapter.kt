import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.darktheme.unitime.AppInfo
import com.darktheme.unitime.R
import com.darktheme.unitime.models.JsonObjects.PostContentObj
import com.darktheme.unitime.models.MyViewHolder
import com.darktheme.unitime.viewModels.PostsViewModel
import java.util.*
import com.bumptech.glide.request.target.Target


/**
 * Created by User on 1/1/2018.
 */
class PostsAdapter(val context: Context, val views: ArrayList<MyViewHolder>, val postsContent: ArrayList<PostContentObj>, val viewTypes : List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val postViewType = 1
        val hierarchyViewType = 2
        val blocktViewType = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == postViewType) {
            return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_single_post, parent, false))
        } else if (viewType == hierarchyViewType) {
            return HierarchyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_posts_hierarchy, parent, false))
        } else {
            return BlockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_posts_block, parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == postViewType) {
            bindPostView(holder as PostViewHolder, position)
        }
        else if (holder.itemViewType == postViewType) {
            bindHierarchyView(holder as HierarchyViewHolder, position)
        } else {
            bindBlockView(holder as BlockViewHolder, position)
        }
    }

    private fun bindPostView(holder : PostViewHolder, position: Int) {
        if (holder.loaded) {return}
        val currentPostObj = views.get(position).post!!
        holder.publisher.text = StringBuilder("Post ID: ").append(currentPostObj.post_id).append(", Publisher: ").append(currentPostObj.publisher).toString()

        if (!currentPostObj.text_content.isNullOrEmpty()) {
            holder.setTextContent(currentPostObj.text_content)
        }
        if(currentPostObj.type.equals(PostsViewModel.ImageType)) {
            holder.setImageContent(currentPostObj.post_id)
        } else {
            holder.removeProgressBar()
            if(currentPostObj.type.equals(PostsViewModel.FileType)) {
                if (currentPostObj.attachment == null) {
                    holder.setFileContent("nameless.pdf")
                } else {
                    holder.setFileContent(currentPostObj.attachment.name)
                }
            }
        }

        holder.loaded = true
    }

    private fun bindHierarchyView(holder : HierarchyViewHolder, position: Int) {
        val currentDirectory = views.get(position).dir!!
        holder.name.text = currentDirectory
    }

    private fun bindBlockView(holder : BlockViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return views.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentParent : LinearLayout = itemView.findViewById(R.id.post_content_layout)
        val publisher : TextView = itemView.findViewById(R.id.post_publisher)
        val parentLayout : RelativeLayout = itemView.findViewById(R.id.post_parent_layout)
        var loaded : Boolean = false

        fun setTextContent(text : String) {
            val postText : TextView = TextView(context)

            postText.text = text
            postText.textSize = 15F
            postText.setTextColor(Color.BLACK)

            val padding_in_dp = 20 // 6 dps
            val scale: Float = context.getResources().getDisplayMetrics().density
            val padding_in_px = (padding_in_dp * scale + 0.5f).toInt()
            postText.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px)
            contentParent.addView(postText)
        }

        fun setFileContent(filename : String) {
            val fileContent: View = LayoutInflater.from(context).inflate(R.layout.layout_file_content, parentLayout, false)

            val filenameTextView = fileContent.findViewById<TextView>(R.id.file_name)
            filenameTextView.text = filename
            filenameTextView.setOnClickListener{
                // TODO ask for the content from the server and open the file
            }
            contentParent.addView(fileContent)
        }

        fun setImageContent(imageID : String) {
            val img = ImageView(context)
            Glide.with(context)
                .load(StringBuilder(AppInfo.serverUrl).append("/post/content?id=").append(imageID).toString()).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    removeProgressBar()
                    contentParent.addView(LayoutInflater.from(context).inflate(R.layout.layout_error_loading, contentParent, false))
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    removeProgressBar()
                    return false
                }
            }).into(img)
            contentParent.addView(img)
        }

        fun removeProgressBar() {
            val progressBar = parentLayout.findViewById<ProgressBar>(R.id.progressBar)
            contentParent.removeView(progressBar)
        }
    }

    inner class HierarchyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.directory_name)
        val parentLayout : RelativeLayout = itemView.findViewById(R.id.directory_parent_layout)
    }

    inner class BlockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}

class ScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        //println("scrolled")
        val pos : Int = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        //println("load 10 more from position: " + pos)
        // check if loaded before loading
        // loading = get the post json obj + post content(if not file)
    }

}