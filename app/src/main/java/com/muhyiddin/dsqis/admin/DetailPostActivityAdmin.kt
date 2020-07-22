package com.muhyiddin.dsqis.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.FragmentArtikel
import com.muhyiddin.dsqis.NewPostActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.CommentAdapter
import com.muhyiddin.dsqis.model.Comment
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_post.*
import kotlinx.android.synthetic.main.activity_detail_post.image_post
import kotlinx.android.synthetic.main.activity_detail_post_admin.*
import java.util.*

class DetailPostActivityAdmin : AppCompatActivity() {

    private val list:MutableList<Comment> = mutableListOf()

    lateinit var post: Post
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var adapter: CommentAdapter
    lateinit var prefs: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = AppPreferences(this)

        post = intent.extras.getSerializable("post") as Post

        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(post.cover)
            .into(image_post)

        title_post_admin.text = post.judul
        nama_penulis_admin.text = String.format(resources.getString(R.string.writer, post?.writerName))
        isi_post_admin.text = post.isi
        tanggal_post_admin.text = post.postDate

        supportActionBar?.title = post.judul


            if(post.status==false ){
                if(post.writerName!=prefs.nama){
                    acc_post.visibility=View.VISIBLE
                }else{
                    acc_post.visibility=View.GONE
                }
            }else if (post.status==true ){
                if(post.writerName!=prefs.nama){
                    acc_post.visibility=View.VISIBLE
                    acc_post.setBackgroundResource(R.drawable.border4)
                    acc_post.setText("Tolak")

                }else{
                    acc_post.visibility=View.GONE
                }
            }


        acc_post.setOnClickListener {
            val status= acc_post.text.toString()
            if(status=="Setujui"){
                val status2=true
                val builder = AlertDialog.Builder(this!!)
                builder.setTitle("Setujui Artikel")
                builder.setMessage("Apakah Anda Yakin Akan Menyetujui Artikel ini?")
                builder.setPositiveButton("YA") { dialog, which ->
                    accPost(status2,post.postId)
                }
                builder.setNegativeButton("TIDAK") { dialog, which ->
                    Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("BATALKAN") { _, _ ->
                    Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
                }
                builder.show()

            }else{
                val status2=false

                val builder = AlertDialog.Builder(this!!)
                builder.setTitle("Setujui Artikel")
                builder.setMessage("Apakah Anda Yakin Akan Membatalkan Persetujuan Artikel ini?")
                builder.setPositiveButton("YA") { dialog, which ->
                    accPost(status2,post.postId)
                }
                builder.setNegativeButton("TIDAK") { dialog, which ->
                    Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("BATALKAN") { _, _ ->
                    Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
                }
                builder.show()

            }
        }



        adapter = CommentAdapter(this, list){comment ->

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

        rv_comment_admin.layoutManager = LinearLayoutManager(this)
        rv_comment_admin.adapter = adapter

        getAllKomentar(post.postId)

        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(currentUser?.photoUrl)
            .into(profile_pic_admin)

        input_komentar_admin.addTextChangedListener(afterTextChanged = {
            if (input_komentar_admin.text.toString().isNotEmpty()){
                submit_komentar_admin.isClickable = true
                submit_komentar_admin.isEnabled = true
            } else{
                submit_komentar_admin.isClickable = false
                submit_komentar_admin.isEnabled = false
            }
        })


        submit_komentar_admin.setOnClickListener(){
            submitKomentar(input_komentar_admin.text.toString(), post.postId)
        }
    }

    private fun accPost(status:Boolean,idPost:String){
        val dbRef = mFirestore.collection("posts")
            dbRef.document(idPost)
                .get()
                .addOnSuccessListener {
                    it.reference.update(
                        "status",status
                    )
                }

    }


    ///////////////////////////////FUNCTION ARTIKEL//////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.detail_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
            R.id.edit_post -> {
                finish()
                startActivity(Intent(this, NewPostActivityAdmin::class.java).putExtra("post", post))
            }
            R.id.hapus_post -> deleteArtikel(post.postId, post.judul)
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteArtikel(id:String,judul:String){
        val builder = AlertDialog.Builder(this!!)
        // Set the alert dialog title
        builder.setTitle("HAPUS DATA")
        builder.setMessage("Apakah Anda Yakin Akan Menghapus Data?")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YA") { dialog, which ->
            hapusArtikel(id,judul)
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("TIDAK") { dialog, which ->
            Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("BATALKAN") { _, _ ->
            Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }



    fun hapusArtikel(id:String, judul:String){
        mDatabase.collection("posts").document(id)
            .delete()
            .addOnSuccessListener {
                mStorage.getReference("posts/$judul-$id").delete()
                    .addOnSuccessListener {
                        mDatabase.collection("posts").document(id).collection("comment")
                            .document()
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this,"Artikel Berhasil dihapus",Toast.LENGTH_SHORT).show()
                                FragmentArtikel()
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
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


    /////////////////////////////////////FUNCTION COMMENT/////////////////////////////////////

    fun submitKomentar(komentar:String, postId:String){
        val firestore = mFirestore.collection("posts").document(postId).collection("comment")
        val key = firestore.document().id
        val comment = Comment(key, komentar,getCurrentDate(), currentUser?.uid.toString(), prefs.nama, currentUser?.photoUrl.toString())
        firestore.document(key)
            .set(comment)
            .addOnSuccessListener {
                Toast.makeText(this@DetailPostActivityAdmin,"Komentar Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show()
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
                        it.commentDate.reversed()
                    }
                    showComment(list)

                }
            }

    }


    fun deleteKomentar(postId:String, komentarId:String){
        mDatabase.collection("posts").document(postId).collection("comment").document(komentarId).delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Komentar Berhasil dihapus", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }



    fun showComment(data: List<Comment>){
        adapter.notifyDataSetChanged()
    }

    fun clearField(){
        input_komentar_admin.text.clear()

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
