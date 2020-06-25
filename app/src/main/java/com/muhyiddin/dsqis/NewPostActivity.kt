package com.muhyiddin.dsqis

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
import android.widget.ScrollView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_new_post.*
import java.util.*

class NewPostActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_new_post)

        supportActionBar?.title = "Buat Diskusi Baru"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = AppPreferences(this)

        if (intent?.getSerializableExtra("post") != null){
            post = intent.getSerializableExtra("post") as Post
            Glide.with(this)
                .asBitmap()
                .load(post?.cover)
                .thumbnail(0.15f)
                .into(add_photo_post)

            judul_post.setText(post?.judul)
            isi_post.setText(post?.isi)
        }


        add_photo_post.setOnClickListener() {
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

    fun publishPost(uri:Uri?, judul:String, isi:String){
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
                    val post = Post(judul,isi,imageLocation,getCurrentDate(), prefs.nama, mAuth.currentUser?.uid, mAuth.currentUser?.photoUrl.toString(), key)
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



    fun updatePost(id:String, uri:Uri?, judulLama: String, judulBaru:String, isi:String){
        var imageLocation = ""
        showLoading()
        val dbRef = mFirestore.collection("posts")
            .document(id)
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
            add_photo_post.setImageBitmap(bitmap)
            add_photo_post.scaleType = ImageView.ScaleType.CENTER_CROP
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
                    scroll_view2.setScrollY(0)
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
        if (judul_post.text.isEmpty()){
            judul_post.setError("Judul belum diisi..")
            judul_post.requestFocus()
            return
        }
        if (isi_post.text.isEmpty()){
            isi_post.setError("Post belum diisi..")
            isi_post.requestFocus()
            return
        }
        onGoing = true
        judul_post.isEnabled = false
        isi_post.isEnabled = false
        if (post==null){
            publishPost(uriImageArtikel,judul_post.text.toString(),isi_post.text.toString())
        } else{
            updatePost(post?.postId.toString(), uriImageArtikel, post?.judul.toString(), judul_post.text.toString(), isi_post.text.toString())
        }

    }

    fun setProfileUser(nama: String?, url: String?) {
     }

    fun showLoading() {
//        info ("masuk")
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
                        Toast.makeText(this@NewPostActivity,"BERHASIL",Toast.LENGTH_SHORT).show()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentArtikel()).commit()
                        finish()

                    }

                })
            }
        }
    }


    override fun onResume() {
        super.onResume()
        setProfileUser(FirebaseAuth.getInstance().currentUser?.displayName,FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
    }


    fun setProgressBarLength(total: Int) {
    }

}
