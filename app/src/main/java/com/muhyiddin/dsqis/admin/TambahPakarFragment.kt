package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.*
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_buat_akun_fragment.*
import kotlinx.android.synthetic.main.activity_tambah_pakar_fragment.*

class TambahPakarFragment : Fragment() {
    private val CHOOSE_IMAGE = 101
    private var onGoing = false
    private var pakar: Pakar? = null
    private var ortu: Ortu? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs: AppPreferences
    private var uriImageArtikel: Uri? = null
    private lateinit var jenispakar:String
    var resolver: ContentResolver?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_tambah_pakar_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foto = view.findViewById<ImageView>(R.id.user_profile_photo)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        prefs= AppPreferences(context)


        val submit = view.findViewById<Button>(R.id.submit_button)
        val jenis_pakar = view.findViewById<Spinner>(R.id.listpakar)
        jenispakar = jenis_pakar.getSelectedItem().toString()



        if (arguments?.getSerializable("pakar") != null){
            pakar = arguments?.getSerializable("pakar") as Pakar
            Glide.with(this)
                .asBitmap()
                .load(pakar?.cover)
                .thumbnail(0.15f)
                .into(user_profile_photo)
        }




        foto.setOnClickListener() {
            showImageChooser()
        }

        submit.setOnClickListener {
            validate()
//            showLoading()
//            setProgressBarLength(45)



        }
    }


    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Pakar"), CHOOSE_IMAGE)
    }

    fun showLoading() {
        progress_bar_layout_pakar.visibility = View.VISIBLE
    }

    fun hideLoading(success:Boolean, message:String) {
        progress_bar_layout_pakar.visibility = View.GONE
        if (!success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(context,"BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunPakarFragment())?.commit()
            if (message=="update"){
                FirebaseDatabase.getInstance().getReference("expert/${pakar?.id}").addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(Post::class.java)
                        Toast.makeText(context,"BERHASIL", Toast.LENGTH_SHORT).show()
                        HomeFragment()
                    }

                })
            }
        }
    }

    fun setProgressBarLength(total: Int) {
    }

    fun submitData(uri:Uri?,namapakar:String,email:String,password:String,jenispakar:String){
        var imageLocation = ""
        showLoading()
        val firestore = mFirestore.collection("expert")
        val key = firestore.document().id
        val ref = mStorage.getReference("expert/$namapakar-$key")
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
                imageLocation = it.result.toString()
                mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener{
//                    prefs.role =2
//                    prefs.uid = it.user?.uid
                    val uid=it.user?.uid
//                    prefs.nama = namapakar
                    val user = User(2, namapakar, uid)
                        mFirestore.collection("user")
                            .document(uid!!)
                            .set(user)
                            .addOnSuccessListener {
//                                val firestore = mFirestore.collection("expert")
//                                val key = firestore.document().id
                                val expert = Pakar(key,namapakar,email,password,jenispakar,imageLocation,uid)
                                firestore.document(key)
                                    .set(expert)
                                    .addOnSuccessListener {
                                        setProgressBarLength(75)
                                        hideLoading(true, "newexpert")
                                    }
                                    .addOnFailureListener {
                                        hideLoading(false, "Error ${it.localizedMessage}")
                                    }
                            }
                            .addOnFailureListener{
                                hideLoading(false, "Error ${it.localizedMessage}")
                            }



                }

            }
        }
    }

    private fun validate() {
        if (namapakar.text!!.isEmpty()){
            namapakar.setError("Nama belum diisi..")
            namapakar.requestFocus()
            return
        }
//        if (tingkatan_kelas.text!!.isEmpty()){
//            tingkatan_kelas.setError("Nama belum diisi..")
//            tingkatan_kelas.requestFocus()
//            return
//        }

        if (emailpakar.text!!.isEmpty()){
            emailpakar.setError("Email belum diisi..")
            emailpakar.requestFocus()
            return
        }
        if (passwordpakar.text!!.isEmpty()){
            passwordpakar.setError("Password belum diisi..")
            passwordpakar.requestFocus()
            return
        }
        onGoing = true
        namapakar.isEnabled = false
        emailpakar.isEnabled=false
        passwordpakar.isEnabled=false
        if (pakar==null){
            submitData(uriImageArtikel,namapakar.text.toString(),emailpakar.text.toString(),passwordpakar.text.toString(),jenispakar)
        } else{
//            updatePost(post?.postId.toString(), uriImageArtikel, post?.judul.toString(), judul_post.text.toString(), isi_post.text.toString())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriImageArtikel = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriImageArtikel)
            user_profile_photo.setImageBitmap(bitmap)
            user_profile_photo.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

}
