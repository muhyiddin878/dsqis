package com.muhyiddin.dsqis.admin

import androidx.appcompat.app.AppCompatActivity
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
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.ListPakarAdapter
import com.muhyiddin.dsqis.model.Pakar
import kotlinx.android.synthetic.main.activity_list_guru_fragment.*

class ListAkunPakarFragment : Fragment() {

    private val mDatabase = FirebaseFirestore.getInstance()
    private val refSemuaPakar = mDatabase.collection("expert")
    private lateinit var adapter: ListPakarAdapter
    private val pakar:MutableList<Pakar> = mutableListOf()
    private val list:MutableList<Pakar> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Daftar Akun Pakar")

        return inflater.inflate(R.layout.activity_list_akun_pakar_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var rv_pakar = view.findViewById(R.id.rv_pakar) as RecyclerView
        rv_pakar.layoutManager =LinearLayoutManager(this.context)


        adapter= ListPakarAdapter(context!!,pakar){pakar ->
            //            startActivity(Intent(context, DetailSiswaActivity::class.java).putExtra("siswa",it))

        }

        rv_pakar.adapter = adapter
        getAllPakar()

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
                showPakar(pakar)
                for (tes2 in pakar)
                    Log.d("ini list pakar", tes2.namapakar)
            }
        }

    }

    fun showPakar(data: List<Pakar>){
        data.let {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

    }


}
