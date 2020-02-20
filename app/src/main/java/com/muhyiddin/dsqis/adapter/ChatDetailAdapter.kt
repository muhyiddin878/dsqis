package com.muhyiddin.dsqis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Chat

class ChatDetailAdapter(val listChat:List<Chat>, val listViewType:List<Int>): RecyclerView.Adapter<ChatDetailAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
//        info("position ${position}, item ${listViewType[position]}")
        return listViewType[position]
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return when (p1){
            0 ->{
                ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.sent_message_layout,p0, false))
            }
            else ->{
                ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.receiver_message_layout,p0, false))
            }
        }
    }

    override fun getItemCount(): Int = listChat.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(listChat[p1])
    }

    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val msg = view.findViewById<TextView>(R.id.chat_message)

        fun bindItem(item:Chat){
            msg.text = item.message
        }
    }
}