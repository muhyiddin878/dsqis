package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Chat
import com.muhyiddin.dsqis.model.ChatList
import com.muhyiddin.dsqis.utils.AppPreferences

import org.w3c.dom.Text
import java.text.FieldPosition

class ChatAdapter(private val ctx: Context,private val listChat:MutableList<ChatList>,
                  val listener:(ChatList)->Unit):RecyclerView.Adapter<ChatAdapter.ChatHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.layout_chat_pakar,
                parent,
                false
            )
        )

    }



    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bindItem(listChat[position], listener)
    }


   inner class ChatHolder(val view: View): RecyclerView.ViewHolder(view) {



        val namaDokter = view.findViewById<TextView>(R.id.nama_pengirim)
        val isiChat = view.findViewById<TextView>(R.id.isi_chat)
        val badgeUnread = view.findViewById<TextView>(R.id.badge_unread_chat)
        val cardChat= view.findViewById<RelativeLayout>(R.id.cardChat)
        val hapus = view.findViewById<TextView>(R.id.hapus)
        val time = view.findViewById<TextView>(R.id.time)


        val prefs = AppPreferences(view.context)

        fun bindItem(item:ChatList, listener:(ChatList)->Unit){



            val chats: MutableList<Chat> = mutableListOf()
            val mDatabase = FirebaseDatabase.getInstance()
            val rootRef = mDatabase.getReference("chat/${item.roomId}/conversation")
            rootRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val chatList = it.getValue(Chat::class.java)
                        if (chatList != null) {
                            if(chatList?.pengirim!=prefs.uid){
                                if (chatList.isRead==false){
//                                    chats.clear()
                                    chats.add(chatList)
                                }

                            }

                            if(chats.size>0){
                                badgeUnread.visibility=View.VISIBLE
                                badgeUnread.setText(chats.size.toString())
                                cardChat.setBackgroundResource(R.color.unread)
                            }else{
                                badgeUnread.visibility=View.GONE
                                badgeUnread.setText("")
                                cardChat.setBackgroundResource(0)
                                chats.clear()
                            }

                        }
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }

            })


            if (prefs.role==1){
                namaDokter.text ="${item.nama_pakar}"
                isiChat.text = item.last_chat
            } else{
                namaDokter.text = "${item.nama_member}"
                isiChat.text = item.last_chat
            }

            time.setText(item.time)

            itemView.setOnClickListener() {
                listener(item)
            }
//            card_chat.setOnLongClickListener {
//                deleteChat()
//                return@setOnLongClickListener true

//        }//            }

        }


    }



}

