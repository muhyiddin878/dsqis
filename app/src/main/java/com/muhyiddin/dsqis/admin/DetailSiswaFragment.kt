package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.ArtikelActivity
import com.muhyiddin.dsqis.FragmentLaporan
import com.muhyiddin.dsqis.MainActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.Siswa
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_new_post.*


class DetailSiswaFragment : Fragment() {
    private lateinit var JK_SISWA: String
    private lateinit var  nama: EditText
    private lateinit var  ttl:EditText
    private lateinit var  alamat:EditText
    private lateinit var  nisn:EditText
    private lateinit var  simpan:Button
    private lateinit var  jk: RadioGroup
    private var  jk2: String=""
    private lateinit var  lk: RadioButton
    private lateinit var  pr:RadioButton
    private lateinit var persiapan_siswa:RadioButton
    private lateinit var pertama_siswa:RadioButton
    private lateinit var kedua_siswa:RadioButton
    private lateinit var bundlesiswa:Bundle

    private var siswa:Siswa? = null
    private val CHOOSE_IMAGE = 101
    var resolver: ContentResolver?=null
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs:AppPreferences
    private var uriImageSiswa: Uri? = null
    lateinit var kls:RadioGroup
    lateinit var kelas_siswa:String
    private var kls2:String=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as AdminActivity).setActionBarTitle("Detail Akun Siswa")
        return inflater.inflate(R.layout.activity_detail_siswa_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foto = view.findViewById<ImageView>(R.id.logo_siswa)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val backStackCount = (activity as AdminActivity).supportFragmentManager.backStackEntryCount
        Log.d("Back Stack Count", "$backStackCount")
        prefs= AppPreferences(context)

        bundlesiswa = Bundle()
        val siswa = arguments?.getSerializable("siswa") as Siswa

        nama=view.findViewById(R.id.nama_siswa)
        ttl=view.findViewById(R.id.tempat_tanggal_lahir)
        alamat=view.findViewById(R.id.alamat_siswa)
        nisn=view.findViewById(R.id.nisn_siswa)
        jk = view.findViewById(R.id.jenis_kelamin_siswa) as RadioGroup
        kls = view.findViewById(R.id.tingkatan_kelas_siswa) as RadioGroup
        persiapan_siswa= view.findViewById(R.id.persiapan_siswa) as RadioButton
        pertama_siswa=view.findViewById(R.id.pertama_siswa) as RadioButton
        kedua_siswa=view.findViewById(R.id.kedua_siswa)
        lk = view.findViewById(R.id.lk_detail) as RadioButton
        pr = view.findViewById(R.id.pr_detail) as RadioButton
        simpan = view.findViewById(R.id.simpan_perubahan_siswa) as Button


        nama.setText(siswa.nama)
        ttl.setText(siswa.ttl)
//        kelas.setText(siswa.kelas)
        alamat.setText(siswa.alamat)
        nisn.setText(siswa.nisn)
        JK_SISWA=siswa.gender
        kelas_siswa=siswa.kelas

        if (JK_SISWA == "Laki-laki") {
            lk.isChecked = true
        } else if (JK_SISWA == "Perempuan") {
            pr.isChecked = true
        }

        if (kelas_siswa == "Kelas Persiapan") {
            persiapan_siswa.isChecked = true
        } else if (kelas_siswa== "Tahun Pertama") {
            pertama_siswa.isChecked = true
        }else if(kelas_siswa=="Tahun Kedua"){
            kedua_siswa.isChecked=true
        }


        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(siswa.cover)
            .into(logo_siswa)

        foto.setOnClickListener() {
            showImageChooser()
        }
        jenis_kelamin_siswa.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.lk_detail -> jk2 = "Laki-laki"
                R.id.pr_detail -> jk2 = "Perempuan"
            }
        })
        tingkatan_kelas_siswa.setOnCheckedChangeListener (RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.persiapan_siswa -> kls2 = "Persiapan"
                R.id.pertama_siswa -> kls2 = "Tahun Pertama"
                R.id.kedua_siswa -> kls2 = "Tahun Kedua"
            }
        })
        simpan.setOnClickListener {
            updateSiswa(siswa!!.id,uriImageSiswa,siswa!!.nama,nama_siswa.text.toString(),tempat_tanggal_lahir.text.toString(),jk2,kls2,alamat_siswa.text.toString(),nisn_siswa.text.toString())

        }


        hapus_akun.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            // Set the alert dialog title
            builder.setTitle("HAPUS DATA")
            builder.setMessage("Are you want to Delete This Data?")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES") { dialog, which ->
                hapusAkun(siswa.id,siswa.nama)
            }
            // Display a negative button on alert dialog
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }


    }

    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Anak"), CHOOSE_IMAGE)
    }

    fun updateSiswa(id:String, uri:Uri?, namaLama: String, namaBaru:String,ttlBaru:String,genderBaru:String,kelasBaru:String,alamatBaru: String,nisnBaru:String){
        var imageLocation = ""
//        showLoading()
        val dbRef = mFirestore.collection("students")
            .document(id)
        val ref = mStorage.getReference("students/$namaLama-$id")
        if (uri!=null){
            ref.putFile(uri).continueWithTask {
//                setProgressBarLength(45)
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
//                    setProgressBarLength(75)

//                    val post = Post(judulBaru,isi,imageLocation,"16 November 2018", mAuth.currentUser?.displayName, mAuth.currentUser?.uid, mAuth.currentUser?.photoUrl.toString(), id)
                    dbRef.update(mapOf(
                        "cover" to imageLocation,
                        "nama" to namaBaru,
                        "ttl" to ttlBaru,
                        "gender" to genderBaru,
                        "kelas" to kelasBaru,
                        "alamat" to alamatBaru,
                        "nisn" to nisnBaru
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
                "nama" to namaBaru,
                "ttl" to ttlBaru,
                "gender" to genderBaru,
                "kelas" to kelasBaru,
                "alamat" to alamatBaru,
                "nisn" to nisnBaru
            )).addOnCompleteListener {
                hideLoading(true, "BERHASIL DI UPDATE")
            }
                .addOnFailureListener {
                    hideLoading(false, "Error ${it.localizedMessage}")
                }
        }

    }

    fun hideLoading(success:Boolean, message:String) {
//        progress_bar_layout.visibility = View.GONE
        if (!success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else{
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunFragment())?.commit()
        }
    }

    private fun validate() {
        if (nama_siswa.text!!.isEmpty()){
            nama_siswa.setError("Nama belum diisi..")
            nama_siswa.requestFocus()
            return
        }
        if (JK_SISWA.isEmpty()){
            jenis_kelamin_siswa.requestFocus()
            return
        }
        if (tempat_tanggal_lahir.text!!.isEmpty()){
            tempat_tanggal_lahir.setError("TTL  belum diisi..")
            tempat_tanggal_lahir.requestFocus()
            return
        }

        if (nisn_siswa.text!!.isEmpty()){
            nisn_siswa.setError("Nama belum diisi..")
            nisn_siswa.requestFocus()
            return
        }
//        if (tingkatan_kelas.text!!.isEmpty()){
//            tingkatan_kelas.setError("Nama belum diisi..")
//            tingkatan_kelas.requestFocus()
//            return
//        }
        if (alamat_siswa.text!!.isEmpty()){
            alamat_siswa.setError("Alamat belum diisi..")
            alamat_siswa.requestFocus()
            return
        }
//        if (nomor.text!!.isEmpty()){
//            nomor.setError("Nama belum diisi..")
//            nomor.requestFocus()
//            return
//        }
//        if (namaortu.text!!.isEmpty()){
//            namaortu.setError("Nama belum diisi..")
//            namaortu.requestFocus()
//            return
//        }
//        if (email.text!!.isEmpty()){
//            email.setError("Email belum diisi..")
//            email.requestFocus()
//            return
//        }
//        if (password.text!!.isEmpty()){
//            password.setError("Password belum diisi..")
//            password.requestFocus()
//            return
//        }

        nama_siswa.isEnabled = false
        tempat_tanggal_lahir.isEnabled = false
        nisn_siswa.isEnabled=false
        alamat_siswa.isEnabled=false
//        nomor.isEnabled=false
//        namaortu.isEnabled=false
//        email.isEnabled=false
//        password.isEnabled=false

        updateSiswa(siswa!!.id,uriImageSiswa,siswa!!.nama,nama_siswa.text.toString(),tempat_tanggal_lahir.text.toString(),jk2,kls2,alamat_siswa.text.toString(),nisn_siswa.text.toString())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriImageSiswa = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriImageSiswa)
            logo_siswa.setImageBitmap(bitmap)
            logo_siswa.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun hapusAkun(id:String,namaSiswa:String){
        mFirestore.collection("students").document(id)
            .delete()
            .addOnSuccessListener {
                mStorage.getReference("students/$namaSiswa-$id").delete()
                    .addOnSuccessListener {
                        mFirestore.collection("parents")
                            .get()
                            .addOnSuccessListener {
                                it.forEach {
                                    it.reference.collection("students")
                                        .document(id)
                                        .delete()
                                        .addOnSuccessListener {
                                            mFirestore.collection("nilai").document(id)
                                                .delete()
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show()
                                                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunFragment())?.commit()
                                                }.addOnFailureListener {
                                                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                                                }
                                        }.addOnFailureListener {
                                            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }



//    override fun onOptionsItemSelected(item: MenuItem): Boolean  {
//
//        val id = item!!.itemId
//        if (id==android.R.id.home){
//            val itung=(activity as AppCompatActivity).supportFragmentManager.getBackStackEntryCount()
//            Log.d("isi itung:",itung.toString())
//            if (itung > 0) {
//                (activity as AppCompatActivity).supportFragmentManager.popBackStackImmediate()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
