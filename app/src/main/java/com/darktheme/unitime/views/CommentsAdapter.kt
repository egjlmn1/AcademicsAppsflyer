import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.R
import java.util.*
import com.darktheme.unitime.OnItemClickListener
import com.darktheme.unitime.models.Retrofit.JsonObjects.CommentObj


/**
 * Created by User on 1/1/2018.
 */
class CommentsAdapter(val context: Context, val comments: ArrayList<CommentObj>, val commentListener: OnItemClickListener) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_post_comment, parent, false))
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
       holder.content.text = comments[position].content
       holder.publisher.text = comments[position].publisher
       holder.date.text = comments[position].date
    }


    override fun getItemCount(): Int {
        return comments.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.comment_content)
        val publisher = itemView.findViewById<TextView>(R.id.comment_publisher)
        val date = itemView.findViewById<TextView>(R.id.comment_date)
    }
}