package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.model.Chat
import com.muhyiddin.dsqis.model.ChatList
import com.muhyiddin.dsqis.model.Pakar
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_new_chat_pakar.*
import java.text.SimpleDateFormat
import java.util.*

class NewChatPakarActivity : AppCompatActivity() {

    val listUser:MutableList<User> = mutableListOf()
    val listNamaPakar:MutableList<String> = mutableListOf()
    var listNamaPakarFiltered:List<String> = mutableListOf()
    val listNamaDokter:MutableList<String> = mutableListOf()
    lateinit var prefs: AppPreferences
    val mDatabase = FirebaseDatabase.getInstance()
    private lateinit var time:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_pakar)

        progress_bar.visibility = View.VISIBLE
        prefs= AppPreferences(this)

        supportActionBar?.title = "Pilih Pakar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rootRef = mDatabase.getReference("chat")
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val chatList = it.getValue(ChatList::class.java)
                    if(chatList!=null){
                        if(chatList.id_member==prefs.uid){
                            listNamaPakar.add(chatList.nama_pakar)
                            Log.d("nama pakar","${listNamaPakar}")
                        }
                    }
                }
            }

        })

        time= SimpleDateFormat("dd-MM-yyy HH:mm:ss").format(Date())


        val ref = FirebaseFirestore.getInstance()
            .collection("user")
            .whereEqualTo("role", 2)
        ref.get()
            .addOnSuccessListener {document ->

                progress_bar.visibility = View.GONE
                for (i in document){
                    val users = i.toObject(User::class.java)
                    listUser.add(users)
                    listNamaDokter.add("Pakar ${users.nama}")
                    Log.d("LIST NAMA DOKTER","${listNamaDokter}")
                }


                listNamaPakarFiltered = listNamaDokter.filter {
                    it !in listNamaPakar
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listNamaPakarFiltered)
                lv_kontak_dokter.adapter = adapter
            }

        lv_kontak_dokter.setOnItemClickListener { adapterView, view, position, l ->

            val ref = FirebaseDatabase.getInstance().getReference("chat")
            val key = ref.push().key.toString()
            val room= key
            val namaPakar=  listNamaDokter[position]
            val namaMember=listUser[position]
            val array= arrayOf("$room","$namaPakar","$namaMember")
            val chatList = ChatList(listNamaDokter[position], listUser[position].userId.toString(), prefs.nama, prefs.uid, "", key,time)
            ref.child(key).setValue(chatList).addOnSuccessListener {
                startActivity(Intent(this, ChatDetailActivity::class.java).putExtra("array",array))
                finish()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
