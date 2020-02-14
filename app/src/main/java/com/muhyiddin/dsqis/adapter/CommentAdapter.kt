package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Comment

class CommentAdapter(private val ctx: Context,
                     private val list:List<Comment>,
                     private val listener:(Comment)->Unit): RecyclerView.Adapter<CommentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }

}

class CommentHolder(val view: View):RecyclerView.ViewHolder(view) {
    private val commenterPic: ImageView = view.findViewById(R.id.commenter_pic)
    private val commenterName: TextView = view.findViewById(R.id.commenter_name)
    private val commentDate: TextView = view.findViewById(R.id.comment_date)
    private val comment: TextView = view.findViewById(R.id.comment)

    fun bindItem(item:Comment, listener:(Comment)->Unit){
        commenterName.text = item.commentWriterName
        commentDate.text = item.commentDate
        comment.text = item.commentText

        Glide.with(view)
            .asBitmap()
            .thumbnail(0.25f)
            .load(item.commentWriterPic)
            .into(commenterPic)

        itemView.setOnClickListener{
            listener(item)
        }
    }
}