package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.adapter.CommentAdapter
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.Comment
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_post.*
import kotlinx.android.synthetic.main.comment_layout.*
import kotlinx.android.synthetic.main.popup_option.*
import java.nio.file.Files.find
import java.util.*


class DetailPostActivity : AppCompatActivity() {

    private val list:MutableList<Comment> = mutableListOf()
    lateinit var prefs: AppPreferences
    lateinit var post: Post
//    private var comment: Comment? = null

    private val mDatabase = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var adapter: CommentAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs= AppPreferences(this)

        post = intent.extras.getSerializable("post") as Post

        Log.d("display name",currentUser?.displayName.toString())

        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(post.cover)
            .into(image_post)

        title_post.text = post.judul
        nama_penulis.text = String.format(resources.getString(R.string.writer, post?.writerName))
        isi_post.text = post.isi
        tanggal_post.text = post.postDate

        supportActionBar?.title = post.judul



        adapter = CommentAdapter(this, list){comment ->

            if (post.writerId==FirebaseAuth.getInstance().currentUser?.uid||comment.commentWriterId==FirebaseAuth.getInstance().currentUser?.uid){
                val view = LayoutInflater.from(this).inflate(R.layout.popup_option, null)
                val builder = AlertDialog.Builder(this)
                    .setView(view)

                val dialog = builder.show()
                val hapus = view.findViewById<TextView>(R.id.hapus)
                hapus.setOnClickListener() {
                    deleteKomentar(post.postId, comment.commentid)
                    dialog.dismiss()
                }
            }

        }

        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = adapter

        getAllKomentar(post.postId)

        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(currentUser?.photoUrl)
            .into(profile_pic)

        input_komentar.addTextChangedListener(afterTextChanged = {
            if (input_komentar.text.toString().isNotEmpty()){
                submit_komentar.isClickable = true
                submit_komentar.isEnabled = true
            } else{
                submit_komentar.isClickable = false
                submit_komentar.isEnabled = false
            }
        })


        submit_komentar.setOnClickListener(){
            submitKomentar(input_komentar.text.toString(), post.postId)
        }
    }


    ///////////////////////////////FUNCTION ARTIKEL//////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (post.writerId== FirebaseAuth.getInstance().currentUser?.uid){
            menuInflater.inflate(R.menu.detail_menu,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
            R.id.edit_post -> {
                finish()
                startActivity(Intent(this, NewPostActivity::class.java).putExtra("post", post))
            }
            R.id.hapus_post -> hapusArtikel(post.postId, post.judul)
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun deletePost(id: String, judul:String) {
//        hapusArtikel(id, judul)
//    }


    fun hapusArtikel(id:String, judul:String){
        mDatabase.collection("posts").document(id)
            .delete()
            .addOnSuccessListener {
                mStorage.getReference("posts/$judul-$id").delete()
                    .addOnSuccessListener {
                    Toast.makeText(this,"Artikel Berhasil dihapus",Toast.LENGTH_SHORT).show()
//                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentArtikel()).commit()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }



    override fun onResume() {
        super.onResume()
//        artikelPresenter.getArtikelById(post.postId)
    }




    fun showArtikel(data: List<Post>) {
        data.let{
            Glide.with(this)
                .asBitmap()
                .thumbnail(0.5f)
                .load(it.get(0)?.cover)
                .into(image_post)

            title_post.text = it.get(0)?.judul
            nama_penulis.text = String.format(resources.getString(R.string.writer, it.get(0)?.writerName))
            isi_post.text = it.get(0)?.isi
            tanggal_post.text = it.get(0)?.postDate
        }
    }




    /////////////////////////////////////FUNCTION COMMENT/////////////////////////////////////

    fun submitKomentar(komentar:String, postId:String){
        val firestore = mFirestore.collection("posts").document(postId).collection("comment")
        val key = firestore.document().id
        val comment = Comment(key, komentar,getCurrentDate(), currentUser?.uid.toString(), prefs.nama, currentUser?.photoUrl.toString())
        firestore.document(key)
            .set(comment)
            .addOnSuccessListener {
                Toast.makeText(this@DetailPostActivity,"Komentar Berhasil Ditambahkan!",Toast.LENGTH_SHORT).show()
                clearField()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    fun getAllKomentar(postId: String){
        mDatabase.collection("posts").document(postId).collection("comment")
            .addSnapshotListener { querySnapshot, error ->
                if(error!=null){
                    Toast.makeText(this, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                if (querySnapshot != null){

                list.clear()
                for (comment in querySnapshot){
                    list.add(comment.toObject(Comment::class.java))
                }
                    list.sortBy {
                        it.commentDate
                    }
                showComment(list)

                }
            }

    }


    fun deleteKomentar(postId:String, komentarId:String){
        mDatabase.collection("posts").document(postId).collection("comment").document(komentarId).delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Komentar Berhasil dihapus",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }



    fun showComment(data: List<Comment>){
        adapter.notifyDataSetChanged()
    }

    fun clearField(){
        input_komentar.text.clear()

    }


    fun getCurrentDate():String{
        val date = Calendar.getInstance().get(Calendar.DATE)
        val month = Calendar.getInstance().get(Calendar.MONTH)+1
        val year = Calendar.getInstance().get(Calendar.YEAR)
        when(month){
            1 -> return "$date Januari $year"
            2 -> return "$date Februari $year"
            3 -> return "$date Maret $year"
            4 -> return "$date April $year"
            5 -> return "$date Mei $year"
            6 -> return "$date Juni $year"
            7 -> return "$date Juli $year"
            8 -> return "$date Agustus $year"
            9 -> return "$date September $year"
            10 -> return "$date Oktober $year"
            11 -> return "$date November $year"
            12 -> return "$date Desember $year"
            else -> return month.toString()
        }
    }

}
