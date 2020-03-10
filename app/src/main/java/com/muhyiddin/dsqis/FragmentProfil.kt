package com.muhyiddin.dsqis

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agritech.adapter.ArtikelAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.model.SavedPost
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login
import kotlinx.android.synthetic.main.activity_fragment_profil.*
import kotlinx.android.synthetic.main.activity_list_akun_fragment.*
import kotlinx.android.synthetic.main.activity_new_chat_pakar.*

class FragmentProfil : Fragment() {

    private val user = FirebaseAuth.getInstance().currentUser
    lateinit var prefs: AppPreferences
    private val list: MutableList<Post> = mutableListOf()
    private val listsemuaArtikel: MutableList<Post> = mutableListOf()
    private val listBookmark: MutableList<String> = mutableListOf()
    private lateinit var adapter: ArtikelAdapter
    private val mDatabase = FirebaseFirestore.getInstance()
    private val refSemuaArtikel = mDatabase.collection("posts")
    private val mAuth = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)


        return inflater.inflate(R.layout.activity_fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).supportActionBar?.title = "Profil"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs= AppPreferences(context)

        rv_profile.layoutManager = LinearLayoutManager(context)

        Glide.with(this)
            .asBitmap()
            .thumbnail(0.25f)
            .load(user?.photoUrl)
            .into(profile_pic)

        profile_name.text = prefs.nama

        edit_profil.setOnClickListener {
            //            startActivity(Intent(context, EditProfilActivity::class.java))
        }



        adapter = ArtikelAdapter(context!!, list) {
            //            startActivity<DetailPostActivity>("post" to it, "source" to "profile")
//            startActivity(Intent(context, DetailPostActivity::class.java).putExtra("post",it).putExtra("source","profile"))
        }

        rv_profile.adapter = adapter
//        getBookmark(listsemuaArtikel)


        artikel_saya.setOnClickListener {
            artikel_saya.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            artikel_saya.setTypeface(artikel_saya.typeface, Typeface.BOLD)
            bookmark.setTextColor(ContextCompat.getColor(context!!, R.color.dark_grey))
            bookmark.setTypeface(artikel_saya.typeface, Typeface.NORMAL)
            rv_profile.adapter = adapter
            getArtikelSaya()
        }


        bookmark.setOnClickListener {
            artikel_saya.setTextColor(ContextCompat.getColor(context!!, R.color.dark_grey))
            artikel_saya.setTypeface(artikel_saya.typeface, Typeface.NORMAL)
            bookmark.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            bookmark.setTypeface(artikel_saya.typeface, Typeface.BOLD)
            rv_profile.adapter = adapter
            getAllArtikel()
            getBookmark(listsemuaArtikel)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profil_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item!!.itemId
            //handle item clicks
            if (id == R.id.edit_profil){
                //do your action here, im just showing toast
                Toast.makeText(context, "Edit Profil", Toast.LENGTH_SHORT).show()
            }
            if (id == R.id.logout){
            val builder = AlertDialog.Builder(context!!)
            // Set the alert dialog title
            builder.setTitle("LOGOUT")
            builder.setMessage("Are you want to Logout ?")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES") { dialog, which ->
                FirebaseAuth.getInstance().signOut()
                prefs.resetPreference()
                startActivity(Intent(context, login::class.java))
                activity?.finish()
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

        return super.onOptionsItemSelected(item)

    }

    override fun onResume() {
        super.onResume()
        Glide.with(this)
            .asBitmap()
            .thumbnail(0.2f)
            .load(user?.photoUrl)
            .into(profile_pic)
        profile_name.text = user?.displayName
        (activity as AppCompatActivity).supportActionBar?.title = prefs.nama
    }

    fun showArtikel(data: List<Post>) {
        data.let {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        count_artikel.text = list.size.toString()
    }

    fun showBookmark(data: List<Post>) {
        data.let {

            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        count_bookmark.text = listBookmark.size.toString()
    }

//     fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when(item?.itemId){
//            android.R.id.home -> {
//                getActivity()?.finish()
//                getActivity()?.onBackPressed()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


    fun getArtikelSaya(){
        refSemuaArtikel.addSnapshotListener { querySnapshot, error ->
            if(error!=null){
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(querySnapshot != null){

                list.clear()
                list.forEach {
                    if (it!=null &&  it.writerId==mAuth?.uid){
                        for (artikel in querySnapshot){
                            list.add(artikel.toObject(Post::class.java))
                        }
                showArtikel(list)
                    }
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
                listsemuaArtikel.clear()
                for (artikel in querySnapshot){
                    listsemuaArtikel.add(artikel.toObject(Post::class.java))
                }
//                showArtikel(list)
            }
        }


    }

    fun getBookmark(listSemuaArtikel:MutableList<Post>){
        mDatabase.collection("user")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .collection("savedPost")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    listBookmark.clear()
                    for (bookmark in querySnapshot) {
                        val idSavedPost = bookmark.toObject(SavedPost::class.java)
                        listBookmark.add(idSavedPost.postId)

                    }
                    val listFiltered=listSemuaArtikel.filter {
                            it.postId in listBookmark
                    }

                    for (tes2 in listFiltered)
                        Log.d("ini list terfilter", tes2.postId)

                    showBookmark(listFiltered)

                }
            }

    }


}
