package com.muhyiddin.dsqis.admin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.ChatDetailActivity
import com.muhyiddin.dsqis.DetailPostActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.ListSiswaAdapter
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.Siswa
import kotlinx.android.synthetic.main.activity_list_akun_fragment.*
import android.R.attr.fragment
import android.R.id.message
import com.muhyiddin.dsqis.FragmentLaporan
import kotlinx.android.synthetic.main.activity_fragment_chat_pakar.*


class ListAkunFragment : Fragment() {

    private val mDatabase = FirebaseFirestore.getInstance()
    private val refSemuaSiswa = mDatabase.collection("students")
    private lateinit var adapter: ListSiswaAdapter
    private val siswa:MutableList<Siswa> = mutableListOf()
    private val list:MutableList<Siswa> = mutableListOf()


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
//        val strtext = arguments!!.getString("siswa")
        var fragment=DetailSiswaFragment()

        var rv_siswaa = view.findViewById(R.id.rv_siswa) as RecyclerView
        rv_siswa.layoutManager = LinearLayoutManager(context)


        getAllSiswa()
        adapter= ListSiswaAdapter(context!!,siswa){

            val bundle = Bundle()
            bundle.putSerializable("siswa", it)
            fragment?.arguments = bundle
            val tx = activity?.supportFragmentManager?.beginTransaction()
            tx?.replace(R.id.screen_area, fragment!!)
            tx?.commit()
        }

        rv_siswaa.adapter = adapter

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
                if (siswa.size>0){
                    showSiswa(siswa)
                }else{
                    showEmptyChat()
                }
            }

        }

    }

    fun showSiswa(data: List<Siswa>){
        data.let {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

    }
    fun showEmptyChat() {
        empty_chat1.visibility = View.VISIBLE
    }
}
