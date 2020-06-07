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
import java.text.FieldPosition
class ChatAdapter(private val listChat:List<ChatList>, val listener:(ChatList)->Unit):RecyclerView.Adapter<ChatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(
            LayoutInflater.from(parent.context).inflate(
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

}
    class ChatHolder(val view: View): RecyclerView.ViewHolder(view) {

        val namaDokter = view.findViewById<TextView>(R.id.nama_pengirim)
        val isiChat = view.findViewById<TextView>(R.id.isi_chat)
//        val waktuChat = view.findViewById<TextView>(R.id.waktu_chat)
        val badgeUnread = view.findViewById<TextView>(R.id.badge_unread_chat)

        val prefs = AppPreferences(view.context)

        fun bindItem(item:ChatList, listener:(ChatList)->Unit){
            if (prefs.role==1){
                namaDokter.text ="${item.nama_pakar}"
                isiChat.text = item.last_chat
            } else{
                namaDokter.text = "Orang Tua ${item.nama_member}"
                isiChat.text = item.last_chat
            }

            itemView.setOnClickListener() {
                listener(item)
            }
        }
    }
