package com.muhyiddin.dsqis.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.ListPakarAdapter
import com.muhyiddin.dsqis.adapter.ListSiswaAdapter
import com.muhyiddin.dsqis.model.Pakar
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.Siswa
import kotlinx.android.synthetic.main.activity_home_fragment.*

class HomeFragment : Fragment() {

    lateinit var uname: String
    private val mDatabase = FirebaseFirestore.getInstance()
    private val refSemuaPakar = mDatabase.collection("expert")
    private val pakar:MutableList<Pakar> = mutableListOf()
    private val refSemuaSiswa = mDatabase.collection("students")
    private val siswa:MutableList<Siswa> = mutableListOf()
    private val refSemuaArtikel = mDatabase.collection("posts")
    private val list:MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uname = arguments!!.getString("USERNAME")
//        (activity as AdminActivity).setActionBarTitle(uname)
        return inflater.inflate(R.layout.activity_home_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetings = view.findViewById<TextView>(R.id.greetings)

        greetings.text = "Selamat Datang, $uname"
        getAllArtikel()
        getAllPakar()
        getAllSiswa()




    }

    fun getAllPakar() {
        refSemuaPakar.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
                pakar.clear()
                for (expert in querySnapshot) {
                    pakar.add(expert.toObject(Pakar::class.java))
                }
                if(jumlah_pakar!=null){

                    jumlah_pakar.setText(pakar.size.toString())
                }

            }

        }

    }

    fun getAllSiswa() {
        refSemuaSiswa.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
                siswa.clear()
                for (student in querySnapshot) {
                    siswa.add(student.toObject(Siswa::class.java))
                }
                if(jumlah_ortu!=null && jumlah_siswa!=null){

                    jumlah_siswa.setText(siswa.size.toString())
                    jumlah_ortu.setText(siswa.size.toString())
                }
            }

        }

    }

    fun getAllArtikel(){
        refSemuaArtikel.addSnapshotListener { querySnapshot, error ->
            if(error!=null){
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(querySnapshot != null){
                list.clear()
                for (artikel in querySnapshot){
                    list.add(artikel.toObject(Post::class.java))
                }
                if(jumlah_artikel!=null){
                    jumlah_artikel.setText(list.size.toString())
                }
            }
        }


    }
}
