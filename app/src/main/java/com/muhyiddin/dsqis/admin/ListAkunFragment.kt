package com.muhyiddin.dsqis.admin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.ListSiswaAdapter
import com.muhyiddin.dsqis.model.Siswa
import kotlinx.android.synthetic.main.activity_list_akun_fragment.*

class ListAkunFragment : Fragment() {



    val siswa:MutableList<Siswa> = mutableListOf()
    private val mDatabase = FirebaseFirestore.getInstance()
    private val refSemuaSiswa = mDatabase.collection("students")
    private lateinit var adapter: ListSiswaAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Daftar Akun Siswa")

        return inflater.inflate(R.layout.activity_list_akun_fragment, null)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_siswa.layoutManager = LinearLayoutManager(context)


        adapter= ListSiswaAdapter(context!!,siswa){siswa ->
//            startActivity(Intent(context, DetailSiswaActivity::class.java).putExtra("siswa",it))

        }
        rv_siswa.adapter = adapter
        getAllSiswa()




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
                showSiswa(siswa)
            }
        }

    }

    fun showSiswa(data: List<Siswa>){
        adapter.notifyDataSetChanged()

    }







}
