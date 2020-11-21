
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.darktheme.unitime.AppInfo
import com.darktheme.unitime.OnItemClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.viewModels.PostsViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*


/**
 * Created by User on 1/1/2018.
 */
class PostsAdapter(val activity: MainPageActivity, val posts: ArrayList<PostObj>, val postListener: OnItemClickListener, val fileListener: OnItemClickListener) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_single_post, parent, false))
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //if (holder.loaded) {return}
        val currentPostObj = posts.get(position)
        holder.bind(currentPostObj, postListener)
    }


    override fun getItemCount(): Int {
        return posts.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postView = PostView(activity, itemView)

        fun bind(post : PostObj, clickListener: OnItemClickListener) {
            postView.setUp(post, fileListener)
            itemView.setOnClickListener {
                clickListener.onItemClicked(Gson().toJson(post))
            }
        }

    }
}

class PostView(val activity: MainPageActivity, itemView: View) {
    val contentParent : LinearLayout? = itemView.findViewById(R.id.post_content_layout)
    val publisher : TextView? = itemView.findViewById(R.id.post_publisher)
    val flair : TextView? = itemView.findViewById(R.id.post_flair)
    val date : TextView? = itemView.findViewById(R.id.post_time)
    val parentLayout : RelativeLayout? = itemView.findViewById(R.id.post_parent_layout)
    var post: PostObj? = null
    var postImage: ImageView? = null
    val postInfo: RelativeLayout? = itemView.findViewById(R.id.post_info)

    fun setUp(p : PostObj, fileListener: OnItemClickListener) {
        post = p
        if ((contentParent == null) || (publisher == null) || (parentLayout == null) || (postInfo == null) || (date == null) || (flair == null)) {
            return
        }
        val profPic = parentLayout.findViewById<ImageView>(R.id.post_profile_pic)
        profPic?.let {
            profPic.setOnClickListener {
                val bundle = bundleOf("name" to p.publisher, "email" to p.publisher_email)
                activity.navController!!.navigate(R.id.action_to_nav_my_profile, bundle)
            }
        }
        flair.text = post!!.flair
        publisher.text = post!!.publisher
        publisher.let {
            publisher.setOnClickListener {
                val bundle = bundleOf("name" to p.publisher, "email" to p.publisher_email)
                activity.navController!!.navigate(R.id.action_to_nav_my_profile, bundle)
            }
        }
        setDate(p.date!!)
        if (!post!!.text_content.isNullOrEmpty()) {
            setTextContent(post!!.text_content!!)
        } else {
            val postText = contentParent.findViewById<TextView>(R.id.post_text)
            postText.text = ""
            postText.visibility = View.GONE
        }
        if(post!!.type.equals(PostsViewModel.ImageType)) {
            setImageContent(post!!.post_id)
        } else {
            val parent = contentParent.findViewById<LinearLayout>(R.id.content_container)
            parent?.let {
                parent.removeAllViews()
            }
        }
        if(post!!.type.equals(PostsViewModel.FileType)) {
            if (post!!.attachment == null) {
                setFileContent("nameless.pdf", fileListener)
            } else {
                setFileContent(post!!.attachment!!.name, fileListener)
            }
        } else {
            val parent = contentParent.findViewById<LinearLayout>(R.id.content_container)
            parent?.let {
                val fileContent = parent.findViewById<View>(R.id.file_contnent)
                fileContent?.let {
                    parent.removeView(fileContent)
                }
            }
        }
    }

    fun setTextContent(text : String) {
        val postText = contentParent!!.findViewById<TextView>(R.id.post_text)
        postText.visibility = View.VISIBLE
        postText.text = text
    }

    fun setDate(dateString: String) {
        val dt = LocalDateTime.parse(dateString.subSequence(0, dateString.length-1))
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val years = dt.until(now, ChronoUnit.YEARS).toInt()
        if (years > 0) {
            date!!.text = StringBuilder().append(years).append(" years ago").toString()
        } else {
            val months = dt.until(now, ChronoUnit.MONTHS).toInt()
            if (months > 0) {
                date!!.text = StringBuilder().append(months).append(" months ago").toString()
            } else {
                val days = dt.until(now, ChronoUnit.DAYS).toInt()
                if (days > 0) {
                    date!!.text = StringBuilder().append(days).append(" days ago").toString()
                } else {
                    val hours = dt.until(now, ChronoUnit.HOURS).toInt()
                    if (hours > 0) {
                        date!!.text = StringBuilder().append(hours).append(" hours ago").toString()
                    } else {
                        val minutes = dt.until(now, ChronoUnit.MINUTES).toInt()
                        if (minutes > 0) {
                            date!!.text = StringBuilder().append(minutes).append(" minutes ago").toString()
                        } else {
                            date!!.text = "now"

                        }
                    }
                }
            }
        }
    }

    fun setFileContent(filename : String, fileListener: OnItemClickListener) {
        val parent = contentParent!!.findViewById<LinearLayout>(R.id.content_container)
        if (parent == null) {
            return
        }
        var fileContent = parent.findViewById<View>(R.id.file_contnent)
        if (fileContent == null) {
            fileContent = LayoutInflater.from(activity).inflate(R.layout.layout_file_content, contentParent, false)
            parent.addView(fileContent)
        }

        val filenameTextView = fileContent.findViewById<TextView>(R.id.file_name)
        filenameTextView.text = filename
        filenameTextView.setOnClickListener{
            fileListener.onItemClicked(post!!.post_id + " " + post!!.attachment!!.name)
        }
    }

    fun setImageContent(imageID : String) {
        val currentContent = contentParent!!.findViewById<LinearLayout>(R.id.content_container)
        if (isProgressBarRemoved()) {
            currentContent.removeAllViews()
            createProgressBar()
        }
        postImage = ImageView(activity)
        val imageViewParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        postImage!!.setLayoutParams(imageViewParams)

        Glide.with(activity)
            .load(StringBuilder(AppInfo.serverUrl).append("/post/content?id=").append(imageID).toString()).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    removeProgressBar()
                    currentContent.addView(LayoutInflater.from(activity).inflate(R.layout.layout_error_loading, contentParent, false))
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    removeProgressBar()
                    currentContent.addView(postImage)
                    return false
                }
            }).override(Target.SIZE_ORIGINAL).into(postImage!!)
    }

    fun removeProgressBar() {
        val parenet = contentParent!!.findViewById<LinearLayout>(R.id.content_container)
        val progressBar = parenet.findViewById<ProgressBar>(R.id.progressBar)
        parenet.removeView(progressBar)
    }

    fun isProgressBarRemoved(): Boolean {
        val parent = contentParent!!.findViewById<LinearLayout>(R.id.content_container)
        parent?.let {
            val progressBar = parent.findViewById<ProgressBar>(R.id.progressBar)
            return (progressBar == null)
        }
        return true
    }

    fun createProgressBar() {
        val progressBar = ProgressBar(activity)
        progressBar.id = R.id.progressBar
        contentParent!!.findViewById<LinearLayout>(R.id.content_container)?.let {
            contentParent.findViewById<LinearLayout>(R.id.content_container)?.addView(progressBar)
        }
    }
}