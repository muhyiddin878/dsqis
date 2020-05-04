package com.muhyiddin.dsqis.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agritech.adapter.ArtikelAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.DetailPostActivity
import com.muhyiddin.dsqis.NewPostActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.adapter.ArtikelAdapterAdmin
import com.muhyiddin.dsqis.model.Post
import kotlinx.android.synthetic.main.activity_artikel.*
import kotlinx.android.synthetic.main.activity_artikel.rv_artikel
import kotlinx.android.synthetic.main.activity_list_artikel_fragment.*
import kotlinx.android.synthetic.main.activity_list_guru_fragment.*

class ListArtikelFragment : Fragment() {

    private lateinit var adapter: ArtikelAdapterAdmin
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance().currentUser
    private val list:MutableList<Post> = mutableListOf()
    private val refSemuaArtikel = mDatabase.collection("posts")
    lateinit var post: Post




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Daftar Artikel")

        return inflater.inflate(R.layout.activity_list_artikel_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_artikel_admin.layoutManager = LinearLayoutManager(this.context)

        adapter = ArtikelAdapterAdmin(context!!,list){

                        startActivity(Intent(context, DetailPostActivityAdmin::class.java).putExtra("post",it))

        }


        rv_artikel_admin.adapter = adapter
        getAllArtikel()

        val fab: FloatingActionButton? = fab2
        fab?.show()

//        fab?.setOnClickListener {
//            startActivity(Intent(context, NewPostActivity::class.java))
//        }


        rv_artikel_admin.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    fab?.hide()
                } else if(dy<0){
                    fab?.show()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    Handler().postDelayed({
//                        fab?.show()
//                    },700)
//                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

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
                showArtikel(list)
            }
        }


    }

    fun showArtikel(data: List<Post>){
        adapter.notifyDataSetChanged()
    }

}
