package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhyiddin.dsqis.model.Chat
import android.R
import android.util.Log
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.firebase.database.*


class ChatDetailAdapter(private val ctx: Context,val listChat:List<Chat>,
                        val listViewType:List<Int>,private val roomId:String,private val idSender:String): RecyclerView.Adapter<ChatDetailAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
//        info("position ${position}, item ${listViewType[position]}")
        return listViewType[position]
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return when (p1){
            0 ->{
                ViewHolder(LayoutInflater.from(p0.context).inflate(com.muhyiddin.dsqis.R.layout.sent_message_layout,p0, false))
            }
            else ->{
                ViewHolder(LayoutInflater.from(p0.context).inflate(com.muhyiddin.dsqis.R.layout.receiver_message_layout,p0, false))
            }
        }



    }

    override fun getItemCount(): Int = listChat.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(listChat[p1])
    }

    inner class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val msg = view.findViewById<TextView>(com.muhyiddin.dsqis.R.id.chat_message)
        private val chat_image: ImageView = view.findViewById(com.muhyiddin.dsqis.R.id.chat_image)
        val mDatabase = FirebaseDatabase.getInstance()

        fun bindItem(item:Chat){
            if(item.type=="text"){
                msg.visibility=View.VISIBLE
                msg.text = item.message
            }else if (item.type=="image"){
                chat_image.visibility=View.VISIBLE
                Glide.with(view)
                    .asBitmap()
                    .thumbnail(0.6f)
                    .load(item?.image)
                    .into(chat_image)

            }
            if(item.pengirim==idSender){
                msg.setOnLongClickListener {
                    unsentChat(item.id)
                    return@setOnLongClickListener true
                }
            }

            val imagePopup = ImagePopup(ctx)
            imagePopup.initiatePopupWithPicasso(item.image)
            chat_image.setOnClickListener {
                imagePopup.viewPopup()
            }


        }

        private fun unsentChat(id:String){
            val view = LayoutInflater.from(ctx).inflate(com.muhyiddin.dsqis.R.layout.popup_option_unsent, null)
            val builder = AlertDialog.Builder(ctx)
                .setView(view)
            val dialog = builder.show()
            val unsent = view.findViewById<TextView>(com.muhyiddin.dsqis.R.id.unsent)
            unsent.setOnClickListener() {
                deleteChat(id)
                dialog.dismiss()
            }


        }

        private fun deleteChat(id: String){
            mDatabase.getReference("chat/${roomId}/conversation")
                .child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(ctx,"Pesan Telah Diurungkan",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(ctx,"Error Gagal Mengurungkan",Toast.LENGTH_SHORT).show()
                            }




        }


    }

}