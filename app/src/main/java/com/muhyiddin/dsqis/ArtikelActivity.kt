package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agritech.adapter.ArtikelAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.model.Post
import kotlinx.android.synthetic.main.activity_artikel.*
import kotlinx.android.synthetic.main.artikel_layout.*
import java.nio.file.Files.find

class ArtikelActivity : AppCompatActivity() {

    private val mDatabase = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    private val mAuth = FirebaseAuth.getInstance().currentUser
    private val list:MutableList<Post> = mutableListOf()

    private val refArtikelSaya = mDatabase.collection("posts")
    private val refSemuaArtikel = mDatabase.collection("posts")

    private lateinit var adapter:ArtikelAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)
        rv_artikel.layoutManager = LinearLayoutManager(this)


        adapter = ArtikelAdapter(this,list){
            startActivity(Intent(this, DetailPostActivity::class.java).putExtra("post",it))

        }

        rv_artikel.adapter = adapter
        getAllArtikel()

        val fab: FloatingActionButton? = fab
        fab?.show()

        fab?.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
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


//    fun getArtikelSaya(){
//        refArtikelSaya.addValueEventListener(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                refArtikelSaya.removeEventListener(this)
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                list.clear()
//                snapshot.children.forEach {
//                    val post = it.getValue(Post::class.java)
//
//                    if (post!=null && post.writerId==mAuth?.uid) list.add(post)
//                }
//                showArtikel(list)
//            }
//
//        })
//    }


//    fun getArtikelById(id:String){
//        refSemuaArtikel.child(id).addValueEventListener(object:ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                refSemuaArtikel.removeEventListener(this)
//            }
//
//             override fun onDataChange(snapshot: DataSnapshot) {
//                val data = snapshot.getValue(Post::class.java)
//                if (data!=null){
//                    showArtikel(listOf(data))
//                }
//            }
//
//        })
//    }


    fun getAllArtikel(){
        refSemuaArtikel.get().addOnSuccessListener {
                list.clear()
                for (artikel in it){
                    list.add(artikel.toObject(Post::class.java))
                }
                showArtikel(list)

        } .addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }




//    fun getArtikelTersimpan(){
//        mDatabase.collection("users/${mAuth?.uid}/saved_post").addValueEventListener(object:ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                mDatabase.collection("users/${mAuth?.uid}/saved_post").removeEventListener(this)
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                list.clear()
//                snapshot.children.forEach {
//                    val post = it.getValue(Post::class.java)
//                    if (post!=null) list.add(post)
//                }
//                showArtikel(list)
//            }
//
//        })
//    }
//
//    fun hapusArtikel(id:String, judul:String){
//        mDatabase.getReference("posts/$id").removeValue().addOnCompleteListener {
//            if (it.isSuccessful){
//                mDatabase.getReference("users").addListenerForSingleValueEvent(object:ValueEventListener{
//                    override fun onCancelled(p0: DatabaseError) {
//
//                    }
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        snapshot.children.forEach {
//                            val ref = it.child("saved_post").child(id).ref
////                            info("REFERENCE = $ref, ID = $id, IT.KEY = ${it.key}")
//                            mDatabase.getReferenceFromUrl(ref.toString()).removeValue().addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    FirebaseStorage.getInstance().getReference("posts/$judul-$id").delete()
//                                    showMessage("Berhasil dihapus")
//                                }
//                            }
//                        }
//                    }
//
//                })
//            }
//        }
//    }

    fun showArtikel(data: List<Post>){
        adapter.notifyDataSetChanged()
    }
    fun showMessage(message:String){}
}
