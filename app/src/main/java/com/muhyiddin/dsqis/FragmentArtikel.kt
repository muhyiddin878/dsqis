package com.muhyiddin.dsqis

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agritech.adapter.ArtikelAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.model.Post
import kotlinx.android.synthetic.main.activity_artikel.*

class FragmentArtikel:Fragment() {



    private lateinit var adapter:ArtikelAdapter
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance().currentUser
    private val list:MutableList<Post> = mutableListOf()

    private val refArtikelSaya = mDatabase.collection("posts")
    private val refSemuaArtikel = mDatabase.collection("posts")
    lateinit var post: Post


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_artikel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Artikel"
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)




        rv_artikel.layoutManager = LinearLayoutManager(context)


        adapter = ArtikelAdapter(context!!,list){

            startActivity(Intent(context, DetailPostActivity::class.java).putExtra("post",it))

        }

        rv_artikel.adapter = adapter
        getAllArtikel()


        val fab: FloatingActionButton? = fab
        fab?.show()

        fab?.setOnClickListener {
            startActivity(Intent(context, NewPostActivity::class.java))
        }

        rv_artikel.addOnScrollListener(object: RecyclerView.OnScrollListener(){

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
    fun showMessage(message:String){}



}