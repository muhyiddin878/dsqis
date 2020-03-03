package com.muhyiddin.dsqis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Pakar

class ListPakarAdapter(private val ctx: Context,
                       private val list:List<Pakar>,
                       private val listener:(Pakar)->Unit): RecyclerView.Adapter<PakarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PakarHolder {
        return PakarHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_pakar, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PakarHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }



}

class PakarHolder(val view: View):RecyclerView.ViewHolder(view) {
    private val pakarPic: ImageView = view.findViewById(R.id.profile_pic_pakar)
    private val pakarName: TextView = view.findViewById(R.id.nama_pakar)
    private val jenis: TextView = view.findViewById(R.id.jenispakar)
//    private val comment: TextView = view.findViewById(R.id.comment)

    fun bindItem(item:Pakar, listener:(Pakar)->Unit){
        pakarName.text = item.namapakar
        jenis.text=item.jenis


        Glide.with(view)
            .asBitmap()
            .thumbnail(0.25f)
            .load(item.cover)
            .into(pakarPic)



        itemView.setOnClickListener{
            listener(item)
        }
    }
}



