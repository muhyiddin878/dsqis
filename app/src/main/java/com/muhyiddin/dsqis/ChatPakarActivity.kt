//package com.muhyiddin.dsqis
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.firestore.FirebaseFirestore
//import com.muhyiddin.dsqis.adapter.ChatAdapter
//import com.muhyiddin.dsqis.model.Chat
//import com.muhyiddin.dsqis.model.ChatList
//import com.muhyiddin.dsqis.model.User
//import com.muhyiddin.dsqis.utils.AppPreferences
//import kotlinx.android.synthetic.main.activity_chat_pakar.*
//
//class ChatPakarActivity : AppCompatActivity() {
//
//    lateinit var prefs: AppPreferences
//    lateinit var adapter:ChatAdapter
//
//    val mFirestore = FirebaseFirestore.getInstance()
//    val listDokter:MutableList<User> = mutableListOf()
//    val mDatabase = FirebaseDatabase.getInstance()
//
//    val listChat:MutableList<ChatList> = mutableListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat_pakar)
//
//
//
//        prefs = AppPreferences(this)
//
//        if (prefs.role==2){
//            start_new_chat.hide()
//            supportActionBar?.title = "Chat"
//        } else{
//            start_new_chat.show()
//            supportActionBar?.title = "Chat Pakar"
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        }
//        adapter = ChatAdapter(listChat){
//            updateUnreadChat(it.roomId)
////            startActivity<ChatDetailActivity>("room_id" to it.roomId)
//            startActivity(Intent(this, ChatDetailActivity::class.java).putExtra("room_id",it.roomId))
//
//        }
////        rv_chat.layoutManager = LinearLayoutManager(this)
////        rv_chat.adapter = adapter
//
//        getAllChatList()
//
//
//        start_new_chat.setOnClickListener() {
//            startActivity(Intent(this,NewChatPakarActivity::class.java))
//        }
//
//
//    }
//
//
//    fun showLoading() {
//        progress_bar.visibility = View.VISIBLE
//    }
//
//    fun hideLoading() {
//        progress_bar.visibility = View.GONE
//    }
//
//    fun showEmptyChat() {
//        empty_chat.visibility = View.VISIBLE
//    }
//
//    fun hideEmptyChat() {
//        empty_chat.visibility = View.GONE
//    }
//
//    fun showListChat(listChat: List<ChatList>) {
//        rv_chat.visibility = View.VISIBLE
//        this.listChat.clear()
//        this.listChat.addAll(listChat)
//        adapter.notifyDataSetChanged()
//    }
//
//    fun hideListChat() {
//        rv_chat.visibility = View.GONE
//    }
//
//
//    fun getAllChatList(){
//        listChat.clear()
//        showLoading()
//        val ref = mDatabase.getReference("chat")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                p0.children.forEach{
//                    val chatList = it.getValue(ChatList::class.java)
//                    if (chatList != null) {
//                        if (prefs.role==2){
//                            if (prefs.uid==chatList.id_pakar){
//                                listChat.add(chatList)
//                            }
//                        } else if (prefs.role==1){
//                            if (prefs.uid==chatList.id_member){
//                                listChat.add(chatList)
//                            }
//                        }
//
//                    }
//                }
//                    hideLoading()
//                if (listChat.size>0){
//                    showListChat(listChat)
//                } else{
//                    showEmptyChat()
//                }
//            }
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })
//    }
//
//    fun updateUnreadChat(roomId:String){
//        val rootRef = mFirestore.collection("chat-dokter").document(roomId).collection("chats")
//        rootRef.get()
//            .addOnSuccessListener {
//                it.forEach {doc ->
//                    val chat = doc.toObject(Chat::class.java)
//                    if (chat.pengirim!=prefs.uid){
//                        rootRef.document(doc.id)
//                            .update("isRead", true)
//                    }
//
//                }
//            }
//        if (prefs.role==2){
//            mFirestore.collection("chat-dokter").document(roomId).update("unread_dokter", 0)
//        } else{
//            mFirestore.collection("chat-dokter").document(roomId).update("unread_ibu", 0)
//        }
//
//    }
//}
