package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import com.muhyiddin.dsqis.model.Ortu
import com.muhyiddin.dsqis.model.Pakar
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.Siswa
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_pakar_fragment.*
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_tambah_pakar_fragment.*

class DetailPakarFragment : Fragment() {

    private val CHOOSE_IMAGE = 101
    private lateinit var bundlesiswa:Bundle
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs: AppPreferences
    private var uriImagePakar: Uri? = null
    private lateinit var jenispakar:String
    private lateinit var jenispakartext:String
    private lateinit var jenis_pakar:Spinner
    var resolver: ContentResolver?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Detail Akun Pakar")
        return inflater.inflate(R.layout.activity_detail_pakar_fragment, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foto = view.findViewById<ImageView>(R.id.logo_pakar)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        prefs= AppPreferences(context)
        bundlesiswa = Bundle()
        val pakar = arguments?.getSerializable("pakar") as Pakar

        val submit = view.findViewById<Button>(R.id.submit_button_pakar)
        jenis_pakar = view.findViewById(R.id.listpakar2)
        jenis_pakar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                jenispakar = jenis_pakar.getSelectedItem().toString()
            }

        }



        Glide.with(this)
            .asBitmap()
            .load(pakar?.cover)
            .thumbnail(0.15f)
            .into(logo_pakar)

        namapakar1.setText(pakar.namapakar)
//        emailpakar1.setText(pakar.email)

        foto.setOnClickListener() {
            showImageChooser()
        }

        delete_button.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            // Set the alert dialog title
            builder.setTitle("HAPUS DATA")
            builder.setMessage("Apakah Anda Yakin Anda Akan Menghapusnya?")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("IYA") { dialog, which ->
                hapusAkun(pakar.id,pakar.namapakar)
            }
            // Display a negative button on alert dialog
            builder.setNegativeButton("TIDAK") { dialog, which ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            // Display a neutral button on alert dialog
            builder.setNeutralButton("BATALKAN") { _, _ ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }

        jenispakartext=pakar.jenis
        val arrayJenisPakar = resources.getStringArray(R.array.jenis_pakar)
        val pos = arrayJenisPakar.indexOf(jenispakartext)
        jenis_pakar.setSelection(pos)

        submit.setOnClickListener {
            updatePakar(pakar!!.id,uriImagePakar,pakar!!.namapakar,namapakar1.text.toString(),jenispakar)

        }

    }

    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Pakar"), CHOOSE_IMAGE)
    }

    fun showLoading() {
        progress_bar_layout_pakar1.visibility = View.VISIBLE
    }

    fun hideLoading(success:Boolean, message:String) {
        progress_bar_layout_pakar1.visibility = View.GONE
        if (!success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(context,"BERHASIL DI UPDATE", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunPakarFragment())?.commit()

        }
    }

    fun setProgressBarLength(total: Int) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriImagePakar = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriImagePakar)
            logo_pakar.setImageBitmap(bitmap)
            logo_pakar.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    fun updatePakar(id:String, uri:Uri?, namaLama: String, namaBaru:String,jenisBaru:String){
        var imageLocation = ""
        val dbRef = mFirestore.collection("expert")
            .document(id)
        val ref = mStorage.getReference("expert/$namaLama-$id")
        if (uri!=null){
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
                    dbRef.update(mapOf(
                        "cover" to imageLocation,
                        "namapakar" to namaBaru,
                        "jenis" to jenisBaru
                    )).addOnSuccessListener {
                        hideLoading(true, "BERHASIL DI UPDATE")
                    }
                        .addOnFailureListener {
                            hideLoading(false, "Error ${it.localizedMessage}")
                        }
                } else{
                    hideLoading(false, "Error ${it.exception?.message}")
                }
            }
        } else{
            dbRef.update(mapOf(
                "namapakar" to namaBaru,
                "jenis" to jenisBaru
            )).addOnCompleteListener {
                hideLoading(true, "BERHASIL DI UPDATE")
            }
                .addOnFailureListener {
                    hideLoading(false, "Error ${it.localizedMessage}")
                }
        }

    }

    private fun hapusAkun(id:String,namaPakar:String){
        mFirestore.collection("expert").document(id)
            .delete()
            .addOnSuccessListener {
                mStorage.getReference("expert/$namaPakar-$id").delete()
                    .addOnSuccessListener {
                        mFirestore.collection("user").document(id)
                            .delete()
                            .addOnSuccessListener {
                                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunPakarFragment())?.commit()
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




}
