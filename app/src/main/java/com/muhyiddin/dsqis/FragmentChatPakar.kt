package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.adapter.ChatAdapter
import com.muhyiddin.dsqis.model.Chat
import com.muhyiddin.dsqis.model.ChatList
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_fragment_chat_pakar.*

class FragmentChatPakar : Fragment() {
    lateinit var prefs: AppPreferences
    lateinit var adapter: ChatAdapter

    val mFirestore = FirebaseFirestore.getInstance()
    val listDokter:MutableList<User> = mutableListOf()
    val mDatabase = FirebaseDatabase.getInstance()

    val listChat:MutableList<ChatList> = mutableListOf()
    val list:MutableList<ChatList> = mutableListOf()
    private lateinit var chat:String
    private lateinit var idRoom:String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_fragment_chat_pakar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        prefs = AppPreferences(context)

        if (prefs.role==2){
            start_new_chat.hide()
            (activity as AppCompatActivity).supportActionBar?.title = "Chat"
        } else{
            start_new_chat.show()
            (activity as AppCompatActivity).supportActionBar?.title = "Chat Pakar"
//            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        adapter = ChatAdapter(requireContext(),listChat){
            val room= it.roomId
            val namaPakar= it.nama_pakar
            val namaMember=it.nama_member
            val array= arrayOf("$room","$namaPakar","$namaMember")

            updateUnreadChat(it.roomId)
            startActivity(Intent(context, ChatDetailActivity::class.java).putExtra("array",array))


        }
        rv_chat.layoutManager = LinearLayoutManager(context)
        rv_chat.adapter = adapter

        getAllChatList()

//        mDatabase.getReference("chat").child(idRoom).addChildEventListener(object :ChildEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//            }
//
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                adapter.notifyDataSetChanged()
//
//            }
//
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) {
//            }
//
//        })
//        onChange()


        start_new_chat.setOnClickListener() {
            startActivity(Intent(context,NewChatPakarActivity::class.java))
        }



    }



    fun getAllChatList(){
        listChat.clear()
        showLoading()
        val ref = mDatabase.getReference("chat")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val chatList = it.getValue(ChatList::class.java)
//                    for (tes2 in chatList)
                    if (chatList != null) {
//                        listChat.add(chatList)
//                        showListChat(listChat)
                        if (prefs.role==2){
                            if (prefs.uid==chatList.id_pakar){
                                 listChat.add(chatList)
                                idRoom=chatList.roomId

                            }
                        }
                        else if (prefs.role==1){
                            if (prefs.uid==chatList.id_member){
                                listChat.add(chatList)
                                idRoom=chatList.roomId
                            }
                        }




                    }
                }
                hideLoading()

                if (listChat.size>0){
                    listChat.sortByDescending { it.last_chat }
                    showListChat(listChat)
                } else{
                    showEmptyChat()
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    fun onChange(){
        mDatabase.getReference("chat").child(idRoom).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                adapter.notifyDataSetChanged()

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    fun updateUnreadChat(roomId:String){
        val rootRef = mDatabase.getReference("chat/${roomId}/conversation")
        rootRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val chatList = it.getValue(Chat::class.java)
                    if (chatList != null) {
                        if(chatList?.pengirim!=prefs.uid){
                            if (chatList.isRead==false){
                                val key=chatList.id
                                Log.d("isi ChatList","${chatList.message}")
                                rootRef.child(key).ref.updateChildren(mapOf(
                                    "read" to true
                                ))
                            }

                        }

                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })


    }


    fun showLoading() {
        prefs.isLoading=true
        progress_bar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progress_bar.visibility = View.GONE
        prefs.isLoading=false
    }

    fun showEmptyChat() {
        empty_chat.visibility = View.VISIBLE
    }

    fun hideEmptyChat() {
        empty_chat.visibility = View.GONE
    }

    fun showListChat(data: List<ChatList>) {
        rv_chat.visibility = View.VISIBLE
        data.let {
        list.clear()
        list.addAll(it)
        adapter.notifyDataSetChanged()
        }
    }

    fun hideListChat() {
        rv_chat.visibility = View.GONE
    }




}



