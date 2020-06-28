package com.muhyiddin.dsqis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.adapter.ChatDetailAdapter
import com.muhyiddin.dsqis.model.Chat
import com.muhyiddin.dsqis.model.Post
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.activity_new_post.*

class ChatDetailActivity : AppCompatActivity() {
    private val CHOOSE_IMAGE = 101
    val listChat: MutableList<Chat> = mutableListOf()
    val listViewType: MutableList<Int> = mutableListOf()
    val chats: MutableList<Chat> = mutableListOf()
    val mDatabase = FirebaseDatabase.getInstance()
    private val mStorage = FirebaseStorage.getInstance()
    lateinit var prefs: AppPreferences
    lateinit var adapter: ChatDetailAdapter
    private var uri: Uri?=null
    lateinit var type:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)


        val array=  intent.getStringArrayExtra("array")
        Log.d("idroom",array[0])
        Log.d("member",array[1])
        Log.d("pakar",array[2])

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = AppPreferences(this)
        if(prefs.role==1){
            supportActionBar?.title = array[1]
        }else{
            supportActionBar?.title = array[2]
        }
        adapter = ChatDetailAdapter(this,listChat, listViewType,array[0])


        rv_chat.layoutManager = LinearLayoutManager(this)
        rv_chat.adapter = adapter



        getChats(array[0])



        mDatabase.getReference("chat/${array[0]}/conversation")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    adapter.notifyDataSetChanged()
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    adapter.notifyDataSetChanged()

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    adapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    adapter.notifyDataSetChanged()
                }

            })

        btn_send_chat.setOnClickListener() {
            if (input_chat.text.isEmpty() &&  uri==null) {
                btn_send_chat.isClickable = false
            } else {
                btn_send_chat.isClickable = true
                sendChat(array[0], input_chat.text.toString())
                input_chat.text.clear()
            }
        }

        btn_send_image.setOnClickListener() {
            showImageChooser()
        }

        scrollDown()

    }

    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), CHOOSE_IMAGE)
    }


    private fun scrollDown() {
        rv_chat.post {
            rv_chat.scrollToPosition(listChat.size-1)
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
            if (it.pengirim == prefs.uid) {
                listViewType.add(0)     // chatnya sendiri
            } else {
                listViewType.add(1)     // chatnya orang lain
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getChats(roomId: String) {
        showLoading()
        val ref = mDatabase.getReference("chat/${roomId}/conversation")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        info("ACTIVITY RESULT $data AND ${data?.data}")
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            info("MASUK COY BISMILLAH")
            uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            chat_image_preview.visibility= View.VISIBLE
            chat_image_preview.setImageBitmap(bitmap)
            chat_image_preview.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }


    fun sendChat(roomId: String, msg: String) {
        var imageLocation = ""
        val ref = mStorage.getReference("chatPakar/$roomId")
        val key:String= mDatabase.getReference("chat/${roomId}/conversation").push().key.toString()
        if (uri != null) {
            type = "image"
            ref.putFile(uri!!).continueWithTask {
                if (!it.isSuccessful) {
                    it.exception?.let {
                        throw it
                    }
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                return@continueWithTask ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    imageLocation = it.result.toString()
                    val chat = Chat(false, msg, imageLocation, prefs.uid, type,key)
                    chat_image_preview.visibility= View.GONE
                    mDatabase.getReference("chat/${roomId}").child("last_chat").setValue("Gambar")
                    mDatabase.getReference("chat/${roomId}/conversation").child(key).setValue(chat)
                        .addOnSuccessListener {
                            getChats(roomId)
                            scrollDown()
                        }
                }
            }
        } else {
            type = "text"
            val chat = Chat(false, msg, imageLocation, prefs.uid, type,key)
            mDatabase.getReference("chat/${roomId}").child("last_chat").setValue(msg)
            mDatabase.getReference("chat/${roomId}/conversation").child(key).setValue(chat)
                .addOnSuccessListener {
                    getChats(roomId)
                    scrollDown()

                }

        }


    }
}
