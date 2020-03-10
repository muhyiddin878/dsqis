package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
//import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Siswa
import kotlinx.android.synthetic.main.activity_buat_akun_fragment.*
import kotlinx.android.synthetic.main.activity_new_post.*
import android.content.ContentResolver
import android.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhyiddin.dsqis.FragmentArtikel
import com.muhyiddin.dsqis.FragmentChatPakar
import com.muhyiddin.dsqis.model.Ortu
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences


class BuatAkunFragment : Fragment() {
    private val CHOOSE_IMAGE = 101
    private var onGoing = false
    private var siswa: Siswa? = null
    private var ortu: Ortu? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs:AppPreferences
    private var uriImageArtikel: Uri? = null
    lateinit var jk:String
    lateinit var jurusan:String
    lateinit var kls:String
    var resolver:ContentResolver?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Buat Siswa Siswa")
        return inflater.inflate(com.muhyiddin.dsqis.R.layout.activity_buat_akun_fragment, null)



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foto = view.findViewById<ImageView>(com.muhyiddin.dsqis.R.id.image_post)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        prefs= AppPreferences(context)



        val submit = view.findViewById<Button>(com.muhyiddin.dsqis.R.id.submit)
        val scroll = view.findViewById<ScrollView>(com.muhyiddin.dsqis.R.id.scroll_view)

        val jenis_kelamin = view.findViewById<RadioGroup>(com.muhyiddin.dsqis.R.id.jenis_kelamin)
        val tingkatan_kelas = view.findViewById<RadioGroup>(com.muhyiddin.dsqis.R.id.tingkatan_kelas)



        (activity as AppCompatActivity).supportActionBar?.title = "Daftar Siswa Baru"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (arguments?.getSerializable("siswa") != null){
            siswa = arguments?.getSerializable("siswa") as Siswa
            Glide.with(this)
                .asBitmap()
                .load(siswa?.cover)
                .thumbnail(0.15f)
                .into(add_photo_post)
        }
        jenis_kelamin.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                com.muhyiddin.dsqis.R.id.lk -> jk = "Laki-laki"
                com.muhyiddin.dsqis.R.id.pr -> jk = "Perempuan"
            }
        })
        tingkatan_kelas.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                com.muhyiddin.dsqis.R.id.persiapan -> kls = "Persiapan"
                com.muhyiddin.dsqis.R.id.pertama -> kls = "Tahun Pertama"
                com.muhyiddin.dsqis.R.id.kedua -> kls = "Tahun Kedua"
            }
        }

        foto.setOnClickListener() {
            showImageChooser()
        }

        submit.setOnClickListener {
            scroll.setScrollY(0)
            validate()

        }


    }




    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Anak"), CHOOSE_IMAGE)
    }

    fun showLoading() {
        progress_bar_layout_siswa.visibility = View.VISIBLE
    }

    fun hideLoading(success:Boolean, message:String) {
        progress_bar_layout_siswa.visibility = View.GONE
        if (!success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(context,"BERHASIL DITAMBAHKAN",Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.replace(com.muhyiddin.dsqis.R.id.screen_area, ListAkunFragment())?.commit()
//            startActivity(Intent(context,HomeFragment::class.java))

//            val ft = fragmentManager!!.beginTransaction()
//            ft.replace(com.muhyiddin.dsqis.R.id.frame_admin, HomeFragment(), "HomeFragmentTag")
//            ft.commit()
//            ft.addToBackStack(null);

        }
    }
    fun setProgressBarLength(total: Int) {
    }


    fun submitData(uri:Uri?,nama:String,jk:String,ttl:String,nisn:String,kls:String,alamat:String,nomor:String,namaortu:String,email:String,password:String){
        var imageLocation = ""
        showLoading()
        val firestore = mFirestore.collection("students")
        val key = firestore.document().id
        val ref = mStorage.getReference("students/$nama-$key")
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
                    prefs.role =1
                    prefs.uid = it.user?.uid
                    prefs.nama = namaortu
                    val user = User(prefs.role, namaortu, prefs.uid)
                    if (prefs.role==1){
                        mFirestore.collection("user")
                            .document(prefs.uid)
                            .set(user)
                            .addOnSuccessListener {
                                val firestore = mFirestore.collection("parents")
                                val key = prefs.uid
                                val ortu = Ortu(key,namaortu,email,password)
                                firestore.document(key)
                                    .set(ortu)
                                    .addOnSuccessListener {
                                        setProgressBarLength(75)
                                        val firestore3 = mFirestore.collection("students")
                                        val key2 = firestore3.document().id
                                        val siswa = Siswa(key2,input_nama.text.toString(),jk, ttl, nisn,kls,alamat,nomor,imageLocation)
                                        firestore3.document(key2)
                                            .set(siswa)
                                            .addOnSuccessListener {
                                                val firestore2 = firestore.document(ortu.id).collection("students")
//                                              val key2 = firestore2.document().id
                                                firestore2.add(siswa)
                                                    .addOnSuccessListener {
                                                        hideLoading(true, "newstudent")
//                                                        HomeFragment()
                                                    }
                                                    .addOnFailureListener {
                                                        hideLoading(false, "Error ${it.localizedMessage}")
                                                    }

                                            }
                                            .addOnFailureListener {
                                                hideLoading(false, "Error ${it.localizedMessage}")
                                            }

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
    }


    private fun validate() {
        if (input_nama.text!!.isEmpty()){
            input_nama.setError("Nama belum diisi..")
            input_nama.requestFocus()
            return
        }
        if (jk.isEmpty()){

            jenis_kelamin.requestFocus()
            return
        }
        if (ttl.text!!.isEmpty()){
            ttl.setError("TTL  belum diisi..")
            ttl.requestFocus()
            return
        }

        if (nisn.text!!.isEmpty()){
            nisn.setError("Nama belum diisi..")
            nisn.requestFocus()
            return
        }
//        if (tingkatan_kelas.text!!.isEmpty()){
//            tingkatan_kelas.setError("Nama belum diisi..")
//            tingkatan_kelas.requestFocus()
//            return
//        }
        if (alamat.text!!.isEmpty()){
            alamat.setError("Alamat belum diisi..")
            alamat.requestFocus()
            return
        }
        if (nomor.text!!.isEmpty()){
            nomor.setError("Nama belum diisi..")
            nomor.requestFocus()
            return
        }
        if (namaortu.text!!.isEmpty()){
            namaortu.setError("Nama belum diisi..")
            namaortu.requestFocus()
            return
        }
        if (email.text!!.isEmpty()){
            email.setError("Email belum diisi..")
            email.requestFocus()
            return
        }
        if (password.text!!.isEmpty()){
            password.setError("Password belum diisi..")
            password.requestFocus()
            return
        }
        onGoing = true
        input_nama.isEnabled = false
        ttl.isEnabled = false
        nisn.isEnabled=false
        alamat.isEnabled=false
        nomor.isEnabled=false
        namaortu.isEnabled=false
        email.isEnabled=false
        password.isEnabled=false
        if (siswa==null){
            submitData(uriImageArtikel,input_nama.text.toString(),jk,ttl.text.toString(),nisn.text.toString(),kls,alamat.text.toString(),nomor.text.toString(),namaortu.text.toString(),email.text.toString(),password.text.toString())
        } else{
//            updatePost(post?.postId.toString(), uriImageArtikel, post?.judul.toString(), judul_post.text.toString(), isi_post.text.toString())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriImageArtikel = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriImageArtikel)
            image_post.setImageBitmap(bitmap)
            image_post.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }




}
