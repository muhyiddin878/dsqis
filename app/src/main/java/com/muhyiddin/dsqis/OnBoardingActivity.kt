package com.muhyiddin.dsqis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.admin.AdminActivity
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {
    val CHOOSE_IMAGE = 101

    private lateinit var uriProfilImage: Uri
    private val mAuth = FirebaseAuth.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs: AppPreferences
    private val mDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        prefs= AppPreferences(this)


        add_photo.setOnClickListener() {
            showImageChooser()
        }

        finish_btn.setOnClickListener() {
            if (progress_bar.visibility== View.VISIBLE){
//                toast("Tunggu sebentar ya kak :)")
            } else{
                if(prefs.role==1){
                    finish()
                    startActivity(Intent(this,MainActivity::class.java))
                }else if(prefs.role==2){
                    finish()
                    startActivity(Intent(this,MainActivityPakar::class.java))
                }else if(prefs.role==3){
                    finish()
                    startActivity(Intent(this,AdminActivity::class.java).putExtra("USERNAME",prefs.nama))
                }
            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriProfilImage = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriProfilImage)
            foto_profile.setImageBitmap(bitmap)

            uploadImage(uriProfilImage)
        }
    }


    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Pofil"), CHOOSE_IMAGE)
    }

    fun setProfileUser(nama: String?, url:String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    fun hideLoading(success:Boolean, message:String) {
        progress_bar.visibility = View.GONE
        if (success){
            finish_btn.isClickable = true
            finish_btn.setBackgroundResource(R.drawable.bg_btn_onboarding)
        }
    }
    fun setProgressBarLength(total: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun uploadImage(uri:Uri){
        showLoading()
        var imageLocation = ""
        val ref = mStorage.getReference("profilepic/${prefs.uid}")

        ref.putFile(uri).continueWithTask {
            if (!it.isSuccessful){
                it.exception?.let {
                    throw it
                }
                hideLoading(true, "Error ${it.exception?.message}")
            }
            return@continueWithTask ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                imageLocation = it.result.toString()
//                dbRef.update(mapOf(
//                    "cover" to imageLocation,
//                    "isi" to isi,
//                    "judul" to judulBaru
//                )).addOnSuccessListener {
//                    hideLoading(true, "BERHASIL DI UPDATE")
//                }
//                    .addOnFailureListener {
//                        hideLoading(false, "Error ${it.localizedMessage}")
//                    }
                val profile =  UserProfileChangeRequest.Builder().setPhotoUri(it.result).build()
                mAuth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                    hideLoading(true, "")
                }
            } else{
                hideLoading(false, "Error ${it.exception?.message}")
            }
        }
    }


}
