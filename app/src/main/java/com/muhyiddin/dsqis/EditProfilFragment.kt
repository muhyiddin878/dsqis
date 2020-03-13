package com.muhyiddin.dsqis

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.admin.AdminActivity
import com.muhyiddin.dsqis.admin.ListAkunPakarFragment
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_edit_profil_fragment.*
import kotlinx.android.synthetic.main.activity_fragment_profil.*
import kotlinx.android.synthetic.main.activity_fragment_profil.profile_pic

class EditProfilFragment : Fragment() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    private var onGoing = false
    val CHOOSE_IMAGE = 101
    private var uriProfilImage: Uri? = null
    lateinit var prefs: AppPreferences
    var resolver: ContentResolver?=null
    private val mAuth = FirebaseAuth.getInstance()
    private val mStorage = FirebaseStorage.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_edit_profil_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Edit Profil"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        prefs= AppPreferences(context)


//        Glide.with(this)
//            .asBitmap()
//            .thumbnail(0.2f)
//            .load(currentUser?.photoUrl)
//            .into(profile_pic_edit)

        nama_pengguna1.setText(prefs.nama)
        edit_photo.setOnClickListener() {
            showImageChooser()
        }

    }


    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Pofil"), CHOOSE_IMAGE)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profil_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  {

        val id = item!!.itemId
        if (id==android.R.id.home){
            (activity as AppCompatActivity).onBackPressed()
        }
        if(id==R.id.confirm){
            validate()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun validate() {
        if (nama_pengguna1.text.isNullOrEmpty()){
            nama_pengguna1.requestFocus()
            nama_pengguna1.setError("Nama wajib diisi")
            return
        }
//        if (email_pengguna.text.toString().isNotEmpty()&&email_pengguna.text.toString()!=FirebaseAuth.getInstance().currentUser?.email){
//            password_lama_pengguna.requestFocus()
//            password_lama_pengguna.setError("Password wajib diisi untuk merubah email")
//            return
//        }
        if (password_pengguna.text.toString().isNotEmpty()&&konfirm_password_pengguna.text.toString().isNotEmpty()&&password_lama_pengguna.text.toString().isEmpty()){
            password_lama_pengguna.requestFocus()
            password_lama_pengguna.setError("Password lama wajib diisi dahulu")
        }
        if (password_pengguna.text.toString()!=konfirm_password_pengguna.text.toString()){
            konfirm_password_pengguna.requestFocus()
            konfirm_password_pengguna.setError("Password konfirmasi harus sesuai !")
            return
        }
//        onGoing = true
        nama_pengguna1.isEnabled = false
        password_lama_pengguna.isEnabled = false
        password_pengguna.isEnabled = false
        konfirm_password_pengguna.isEnabled = false
        edit_photo.isEnabled = false
        edit_photo.isClickable = false
//        if (password_pengguna.text.isNullOrEmpty() && konfirm_password_pengguna.text.isNullOrEmpty()){
        uriProfilImage.let {uri ->
            if (password_lama_pengguna.text.toString().isNotEmpty() && konfirm_password_pengguna.text.toString().isNotEmpty() && password_pengguna.text.toString().isNotEmpty()){
                reAuth()?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("masuk ","masukk")
//                        info("EDIT PASSWORD")
                        updateImage(uri,nama_pengguna1.text.toString(),password_pengguna.text?.toString())
                    } else{
//                        toast("Error, password salah")
                        onGoing = false
                        nama_pengguna1.isEnabled = true
                        password_lama_pengguna.isEnabled = true
                        password_pengguna.isEnabled = true
                        konfirm_password_pengguna.isEnabled = true
                        edit_photo.isEnabled = true
                        edit_photo.isClickable = true
                    }
                }
            }
            else{
//                info("NOT EDIT PASSWORD")
                updateImage(uri,nama_pengguna1.text.toString(),null)
            }
        }
//        }
//        else{
//            uriProfilImage.let {
//                presenter.updateImage(it,nama_pengguna.text.toString(), email_pengguna.text.toString(), password_pengguna.text.toString())
//            }
//        }

    }

    private fun reAuth() =
        FirebaseAuth.getInstance().currentUser?.reauthenticate(EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser?.email.toString(), password_lama_pengguna.text.toString()))

    fun showLoading() {
//        progress_loading.visibility = View.VISIBLE
    }

    fun hideLoading(success: Boolean, message: String) {
//        progress_loading.visibility = View.GONE
        Toast.makeText(context, "Profil Berhasil Di Ubah", Toast.LENGTH_SHORT)
            .show()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_container, FragmentProfil())?.commit()
//        if (success)
//            onBackPressed()
//        else toast("Error $message")
    }

    fun setProgressBarLength(total: Int) {

    }


    fun updateImage(uri:Uri?, nama:String, newPassword:String?){
        showLoading()
        val ref = mStorage.getReference("profilepic/${prefs.uid}")
        if (uri != null) {
            ref.putFile(uri).continueWithTask {
                if (!it.isSuccessful){
                    it.exception?.let {
                        throw it
                    }
                    hideLoading(true, "Error ${it.exception?.message}")
                }
                return@continueWithTask ref.downloadUrl
            }.addOnCompleteListener {
                Log.d("masuk 1","masukk")
                if (it.isSuccessful){
                    val profile =  UserProfileChangeRequest.Builder()
                        .setPhotoUri(it.result)
                        .setDisplayName(nama)
                        .build()
//                    if (email!=mAuth.currentUser?.email){
//                        mAuth.currentUser?.updateEmail(email)
//                    }
                    mAuth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                        Log.d("masuk 2","masukk")
                        if (newPassword!=null){
                            mAuth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
                                if (it.isSuccessful){
                                    Log.d("masuk 3","masukk")
                                    mAuth.currentUser?.reauthenticate(EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser?.email.toString(), newPassword))
                                }
                            }

                        }
                        hideLoading(true, "")
                    }
                } else{
//                    info(it.exception?.message)
                    hideLoading(false, "Error ${it.exception?.message}")
                }
            }
        }
        else{
            val profile =  UserProfileChangeRequest.Builder()
                .setDisplayName(nama)
                .build()
            mAuth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                if (newPassword!=null){
                    mAuth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
                        if (it.isSuccessful){

                            mAuth.currentUser?.reauthenticate(EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser?.email.toString(), newPassword))
                        }
                    }

                }
                hideLoading(true, "")
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriProfilImage = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriProfilImage)
            profile_pic_edit.setImageBitmap(bitmap)
            profile_pic_edit.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    fun onBackPressed() {
        return
    }




}
