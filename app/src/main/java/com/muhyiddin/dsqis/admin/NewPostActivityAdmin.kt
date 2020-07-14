package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.FragmentArtikel
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.activity_new_post.progress_bar_layout
import kotlinx.android.synthetic.main.activity_new_post.scroll_view2
import kotlinx.android.synthetic.main.activity_new_post_admin.*
import java.util.*

class NewPostActivityAdmin : AppCompatActivity() {
    private val CHOOSE_IMAGE = 101
    private var onGoing = false
    private var post: Post? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    private var uriImageArtikel: Uri? = null
    lateinit var prefs: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post_admin)

        supportActionBar?.title = "Buat Artikel Baru"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = AppPreferences(this)

        if (intent?.getSerializableExtra("post") != null){
            post = intent.getSerializableExtra("post") as Post
            Glide.with(this)
                .asBitmap()
                .load(post?.cover)
                .thumbnail(0.15f)
                .into(add_photo_post_admin)

            judul_post_admin.setText(post?.judul)
            isi_post_admin.setText(post?.isi)
        }


        add_photo_post_admin.setOnClickListener() {
            showImageChooser()
        }
    }
    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Pofil"), CHOOSE_IMAGE)
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

    fun publishPost(uri: Uri?, judul:String, isi:String){
        var imageLocation = ""
        showLoading()

        val firestore = mFirestore.collection("posts")
        val key = firestore.document().id
        val ref = mStorage.getReference("posts/$judul-$key")
        if (uri!=null) {
            ref.putFile(uri).continueWithTask {
                setProgressBarLength(45)
                if (!it.isSuccessful){
                    it.exception?.let {
                        throw it
                    }
                    hideLoading(false, "Error ${it.exception?.localizedMessage}")
                }
                return@continueWithTask ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    imageLocation = it.result.toString()
                    setProgressBarLength(75)
                    val post = Post(judul,isi,imageLocation,getCurrentDate(), "Admin", mAuth.currentUser?.uid, mAuth.currentUser?.photoUrl.toString(), key)
                    firestore.document(key)
                        .set(post)
                        .addOnSuccessListener {
                            hideLoading(true, "newpost")
                            FragmentArtikel()
                        }
                        .addOnFailureListener {
                            hideLoading(false, "Error ${it.localizedMessage}")
                        }
                }
            }
        }
    }



    fun updatePost(id:String, uri: Uri?, judulLama: String, judulBaru:String, isi:String){
        var imageLocation = ""
        showLoading()
        val dbRef = mFirestore.collection("posts")
            .document(id)
        val savedDbRef = mFirestore.collection("user")
        val ref = mStorage.getReference("posts/$judulLama-$id")
//        info("UPDATE MASUK LORD, URI -> $uri")
        if (uri!=null){
//            info("URI NOT NULL")
            ref.putFile(uri).continueWithTask {
                setProgressBarLength(45)
                if (!it.isSuccessful){
                    it.exception?.let {
                        throw it
                    }
                    hideLoading(false, "Error ${it.exception?.message}")
                }
                return@continueWithTask ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    imageLocation = it.result.toString()
                    setProgressBarLength(75)

//                    val post = Post(judulBaru,isi,imageLocation,"16 November 2018", mAuth.currentUser?.displayName, mAuth.currentUser?.uid, mAuth.currentUser?.photoUrl.toString(), id)
                    dbRef.update(mapOf(
                        "cover" to imageLocation,
                        "isi" to isi,
                        "judul" to judulBaru
                    )).addOnSuccessListener {
                        hideLoading(true, "BERHASIL DI UPDATE")
                    }
                        .addOnFailureListener {
                            hideLoading(false, "Error ${it.localizedMessage}")
                        }
                } else{
//                    info(it.exception?.message)
                    hideLoading(false, "Error ${it.exception?.message}")
                }
            }
        } else{
//            info("URI NULL")
            dbRef.update(mapOf(
                "isi" to isi,
                "judul" to judulBaru
            )).addOnCompleteListener {
                hideLoading(true, "BERHASIL DI UPDATE")
            }
                .addOnFailureListener {
                    hideLoading(false, "Error ${it.localizedMessage}")
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        info("ACTIVITY RESULT $data AND ${data?.data}")
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
//            info("MASUK COY BISMILLAH")
            uriImageArtikel = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriImageArtikel)
            add_photo_post_admin.setImageBitmap(bitmap)
            add_photo_post_admin.scaleType = ImageView.ScaleType.CENTER_CROP
//            presenter.publishPost(uriImageArtikel)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.publish_post ->{
                if (!onGoing){
                    scroll_view3.setScrollY(0)
                    validate()
                    return true
                } else{
                    return super.onOptionsItemSelected(item)
                }

            }
            android.R.id.home -> {
                if (!onGoing){
                    onBackPressed()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun validate() {
        if (judul_post_admin.text.isEmpty()){
            judul_post_admin.setError("Judul belum diisi..")
            judul_post_admin.requestFocus()
            return
        }
        if (isi_post_admin.text.isEmpty()){
            isi_post_admin.setError("Post belum diisi..")
            isi_post_admin.requestFocus()
            return
        }
        onGoing = true
        judul_post_admin.isEnabled = false
        isi_post_admin.isEnabled = false
        if (post==null){
            publishPost(uriImageArtikel,judul_post_admin.text.toString(),isi_post_admin.text.toString())
        } else{
            updatePost(post?.postId.toString(), uriImageArtikel, post?.judul.toString(), judul_post_admin.text.toString(), isi_post_admin.text.toString())
        }

    }

    fun setProfileUser(nama: String?, url: String?) {
    }

    fun showLoading() {
        progress_bar_layout.visibility = View.VISIBLE
    }

    fun hideLoading(success:Boolean, message:String) {
        progress_bar_layout.visibility = View.GONE
        if (!success) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else{
            this.finish()
            if (message=="update"){
                FirebaseDatabase.getInstance().getReference("posts/${post?.postId}").addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(Post::class.java)
                        Toast.makeText(this@NewPostActivityAdmin,"BERHASIL", Toast.LENGTH_SHORT).show()
                        supportFragmentManager.beginTransaction().replace(R.id.screen_area, ListArtikelFragment()).commit()
                        finish()

                    }

                })
            }
        }
    }


    override fun onResume() {
        super.onResume()
        setProfileUser(FirebaseAuth.getInstance().currentUser?.displayName, FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
    }


    fun setProgressBarLength(total: Int) {
    }
}
