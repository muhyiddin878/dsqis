package com.agritech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhyiddin.dsqis.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.model.Comment
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.SavedPost
import de.hdodenhof.circleimageview.CircleImageView


class ArtikelAdapter(private val context: Context, private val list:List<Post>, private val listener:(Post)->Unit):RecyclerView.Adapter<ArtikelAdapter.PostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelAdapter.PostHolder {
        return PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.artikel_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArtikelAdapter.PostHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }

    class PostHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val imagePost: ImageView = view.findViewById(R.id.image_post)
        private val titlePost: TextView = view.findViewById(R.id.title_post)
        private val profilePic: CircleImageView = view.findViewById(R.id.profile_pic)
        private val username: TextView = view.findViewById(R.id.nama_pengguna)
        private val postDate: TextView = view.findViewById(R.id.post_date)
        private val bookmark: ImageButton = view.findViewById(R.id.bookmark)
        private val mDatabase = FirebaseFirestore.getInstance()
        private val refSemuaBookmark = mDatabase.collection("posts")

//        lateinit var post: Post

        fun bindItem(item: Post, listener: (Post) -> Unit) {
            checkBookmark(item)
//            post = intent.extras.getSerializable("post") as Post


            titlePost.text = item.judul
            postDate.text = item.postDate
            username.text = item.writerName
            Glide
                .with(view)
                .asBitmap()
                .load(item.cover)
                .thumbnail(0.5f)
                .into(imagePost)

            Glide
                .with(view)
                .asBitmap()
                .thumbnail(0.25f)
                .load(item.writerPic)
                .into(profilePic)

            bookmark.setOnClickListener() {

                //                val refSemuaArtikel = mDatabase.collection("posts").document("postId").get()
                val list: MutableList<SavedPost> = mutableListOf()
                mDatabase.collection("user")
                    .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .collection("savedPost")
                    .get().addOnSuccessListener {
                        list.clear()
                        for (bookmark in it) {
                            list.add(bookmark.toObject(SavedPost::class.java))
                        }
                        var ketemu: Boolean = false
                        list.forEach {
                            if (it.postId == item.postId) {
                                ketemu = true
                                removeBookmark(it.savedPostId)
                            }
                        }
                        if (!ketemu) {
                            addBookmark(item.postId)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(view.context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }


            }

            itemView.setOnClickListener() {
                listener(item)
            }
        }

        private fun addBookmark(post: String) {

            val firestore = mDatabase.collection("user")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .collection("savedPost")
            val key = firestore.document().id
            val saved = SavedPost(key, post)
            firestore.document(key).set(saved)

                .addOnSuccessListener {
                    Toast.makeText(view.context, "Artikel Berhasil Di Simpan!", Toast.LENGTH_SHORT)
                        .show()

                }.addOnFailureListener {
                    Toast.makeText(view.context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }


        }

        private fun removeBookmark(key: String) {
            val firestore = mDatabase.collection("user")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .collection("savedPost")
            val key = firestore.document().id
            firestore.document(key).delete()
                .addOnSuccessListener {
                    Toast.makeText(view.context, "Dihapus dari Bookmark", Toast.LENGTH_SHORT).show()
                    bookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            view.context,
                            R.drawable.ic_bookmark_border_black_24dp
                        )
                    )
                }


//            FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/saved_post/$key").removeValue().addOnCompleteListener {
//                if (it.isSuccessful){
//                    Toast.makeText(view.context, "Dihapus dari Bookmark", Toast.LENGTH_SHORT).show()
//                    bookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_border_black_24dp))
//                }
//            }
        }

        private fun checkBookmark(post: Post) {
            val list: MutableList<SavedPost> = mutableListOf()

            mDatabase.collection("user")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .collection("savedPost")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        list.clear()
                        for (bookmark in querySnapshot) {
                            list.add(bookmark.toObject(SavedPost::class.java))
                        }
                        list.forEach {
                            if (it.postId == post.postId) {
                                bookmark.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        view.context,
                                        R.drawable.ic_bookmark_black_24dp
                                    )
                                )
                            }

                        }
                    }
                }

        }

    }
}
