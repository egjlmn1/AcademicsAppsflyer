import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darktheme.unitime.OnItemClickListener
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.GetCommentObj
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/**
 * Created by User on 1/1/2018.
 */
class CommentsAdapter(val context: Context, val comments: List<GetCommentObj>, val commentListener: OnItemClickListener) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_post_comment, parent, false))
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.content.text = comments[position].content
        holder.publisher.text = comments[position].publisher

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        val dt = LocalDateTime.parse(comments[position].date, formatter);
        println("DATE TIME: " + dt.toString())
        val now = LocalDateTime.now(ZoneOffset.UTC)
        println("DATE TIME NOW: " + now.toString())

        val years = dt.until(now, ChronoUnit.YEARS).toInt();
        if (years > 0) {
            holder.date.text = StringBuilder().append(years).append(" years ago").toString()
        } else {
            val months = dt.until(now, ChronoUnit.MONTHS).toInt();
            if (months > 0) {
                holder.date.text = StringBuilder().append(months).append(" months ago").toString()
            } else {
                val days = dt.until(now, ChronoUnit.DAYS).toInt();
                if (days > 0) {
                    holder.date.text = StringBuilder().append(days).append(" days ago").toString()
                } else {
                    val hours = dt.until(now, ChronoUnit.HOURS).toInt();
                    if (hours > 0) {
                        print("hours: " + hours)
                        holder.date.text = StringBuilder().append(hours).append(" hours ago").toString()
                    } else {
                        val minutes = dt.until(now, ChronoUnit.MINUTES).toInt();
                        if (minutes > 0) {
                            holder.date.text = StringBuilder().append(minutes).append(" minutes ago").toString()
                        } else {
                            holder.date.text = "now"

                        }
                    }
                }
            }
        }
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