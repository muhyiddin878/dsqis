package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Post

class ArtikelAdapterAdmin (private val context: Context, private val list:List<Post>, private val listener:(Post)->Unit):
    RecyclerView.Adapter<ArtikelAdapterAdmin.ArtikelHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelHolder {
        return ArtikelHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_list_artikel_admin,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArtikelHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }

    class ArtikelHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val imagePost: ImageView = view.findViewById(R.id.pic_artikel)
        private val titlePost: TextView = view.findViewById(R.id.judul_artikel)
        private val username: TextView = view.findViewById(R.id.writer)
        private val tanggal: TextView = view.findViewById(R.id.tanggal_artikel)
        private val mDatabase = FirebaseFirestore.getInstance()
        private val refSemuaBookmark = mDatabase.collection("posts")

//        lateinit var post: Post

        fun bindItem(item: Post, listener: (Post) -> Unit) {


            titlePost.text = item.judul
            tanggal.text = item.postDate
            username.text = item.writerName
            Glide
                .with(view)
                .asBitmap()
                .load(item.cover)
                .thumbnail(0.5f)
                .into(imagePost)

            itemView.setOnClickListener() {
                listener(item)
            }
        }


    }
}