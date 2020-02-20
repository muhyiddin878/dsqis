package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.ChatList
import com.muhyiddin.dsqis.utils.AppPreferences
import org.w3c.dom.Text

class ChatAdapter(private val listChat:List<ChatList>, val listener:(ChatList)->Unit):RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        return ChatAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_fragment_chat_pakar,
                parent,
                false
            )
        )
    }

//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.layout_chat_pakar, p0, false))
//    }

    override fun getItemCount(): Int = listChat.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(listChat[p1], listener)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        val namaDokter = view.findViewById<TextView>(R.id.nama_pengirim)
        val isiChat = view.findViewById<TextView>(R.id.isi_chat)
        val waktuChat = view.findViewById<TextView>(R.id.waktu_chat)
        val badgeUnread = view.findViewById<TextView>(R.id.badge_unread_chat)

        val prefs = AppPreferences(view.context)

        fun bindItem(item:ChatList, listener:(ChatList)->Unit){
            if (prefs.role==1){
                namaDokter.text = "Orang Tua ${item.nama_member}"
                isiChat.text = item.last_chat
            } else{
                namaDokter.text = "Dokter ${item.nama_pakar}"
                isiChat.text = item.last_chat
            }

            itemView.setOnClickListener() {
                listener(item)
            }
        }
    }
}