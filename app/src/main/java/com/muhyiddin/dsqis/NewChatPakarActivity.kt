package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.model.ChatList
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_new_chat_pakar.*

class NewChatPakarActivity : AppCompatActivity() {

    val listUser:MutableList<User> = mutableListOf()
    val listNamaDokter:MutableList<String> = mutableListOf()
    lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_pakar)



        progress_bar.visibility = View.VISIBLE
        prefs= AppPreferences(this)

        supportActionBar?.title = "Pilih Pakar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listNamaDokter)
                lv_kontak_dokter.adapter = adapter
            }

        lv_kontak_dokter.setOnItemClickListener { adapterView, view, position, l ->
            val ref = FirebaseFirestore.getInstance().collection("chat")
            val key = ref.document().id
//            val key = ref.push().key.toString()
            val chatList = ChatList(listNamaDokter[position], listUser[position].userId.toString(), "Muhyiddin", prefs.uid, "", key)
            ref.document(key).set(chatList)
            .addOnSuccessListener {
                startActivity(Intent(this, ChatDetailActivity::class.java).putExtra("room_id",key))
//                startActivity<ChatDetailActivity>("room_id" to key)
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
