package com.muhyiddin.dsqis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.adapter.ChatDetailAdapter
import com.muhyiddin.dsqis.model.Chat
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_chat_detail.*

class ChatDetailActivity : AppCompatActivity() {
    val listChat:MutableList<Chat> = mutableListOf()
    val listViewType:MutableList<Int> = mutableListOf()
    val chats:MutableList<Chat> = mutableListOf()
    val mDatabase = FirebaseDatabase.getInstance()
    lateinit var prefs: AppPreferences
//    lateinit var presenter:ChatDetailPresenter
    lateinit var adapter: ChatDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat"

        val roomId = intent.getStringExtra("room_id")

        prefs = AppPreferences(this)
        adapter = ChatDetailAdapter(listChat, listViewType)

        rv_chat.layoutManager = LinearLayoutManager(this)
        rv_chat.adapter = adapter

        getChats(roomId)

        scrollDown()

        btn_send_chat.setOnClickListener() {
            if (input_chat.text.isEmpty()){
                Toast.makeText(this,"Ketikkan Pesan Terlebih Dahulu",Toast.LENGTH_SHORT).show()
            } else{
                sendChat(roomId, input_chat.text.toString())
                input_chat.text.clear()
            }
        }

    }

    private fun scrollDown(){
        rv_chat.post {
            rv_chat.scrollToPosition(listChat.size)
        }
    }

    fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    fun showChats(chats: List<Chat>) {
        listChat.clear()
        listChat.addAll(chats)
        listChat.forEach {
            if (it.pengirim==prefs.uid){
                listViewType.add(0)     // chatnya sendiri
            } else{
                listViewType.add(1)     // chatnya orang lain
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getChats(roomId: String){
        showLoading()
        val ref = mDatabase.getReference("chat/${roomId}/conversation")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                chats.clear()
                p0.children.forEach {
                    val data = it.getValue(Chat::class.java)
                    if (data != null) {
                        chats.add(data)
                    }
                }

                showChats(chats)
                hideLoading()
            }

        })
    }

//    fun getChats(roomId: String){
//        showLoading()
//        val ref = mDatabase.collection("chat").document(roomId).collection("conversation")
//        ref.addSnapshotListener { querySnapshot, error ->
//            if(error!=null){
//                Toast.makeText(this, error?.localizedMessage, Toast.LENGTH_SHORT).show()
//                return@addSnapshotListener
//            }
//            if(querySnapshot!=null){
//                chats.clear()
////                val data = querySnapshot.getDocumentChanges(Chat::class.java)
//                for (doc in querySnapshot.getDocumentChanges()) {
//                    chats.add(doc as Chat)
//                }
//            }
//                showChats(chats)
//                hideLoading()
//            }
//
//        }



//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                chats.clear()
//                p0.children.forEach {
//                    val data = it.getValue(Chat::class.java)
//                    if (data != null) {
//                        chats.add(data)
//                    }
//                }
//
//                showChats(chats)
//                hideLoading()
//            }
//
//        })



    fun sendChat(roomId:String, msg:String){
        val chat = Chat(false,msg,prefs.uid)
        mDatabase.getReference("chat/${roomId}").child("last_chat").setValue(msg)
        mDatabase.getReference("chat/${roomId}/conversation").push().setValue(chat).addOnSuccessListener {
            getChats(roomId)
        }
    }
//    fun sendChat(roomId:String, msg:String){
//        val firestore2= mDatabase.collection("chat").document(roomId).collection("last_chat")
//            firestore2.document().set(msg)
//        val firestore= mDatabase.collection("chat").document(roomId).collection("conversation")
//        val key = firestore.document().id
//        val chat = Chat(false,msg,prefs.uid)
//        firestore.document(key).set(chat)
////            .add(chat)
//            .addOnSuccessListener {
//            getChats(roomId)
//        }
//    }


}
