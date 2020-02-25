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
import com.muhyiddin.dsqis.model.Siswa

class ListSiswaAdapter(private val ctx: Context,
                       private val list:List<Siswa>,
                       private val listener:(Siswa)->Unit): RecyclerView.Adapter<SiswaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaHolder {
        return SiswaHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_siswa, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SiswaHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }

}


class SiswaHolder(val view: View):RecyclerView.ViewHolder(view) {
    private val siswaPic: ImageView = view.findViewById(R.id.profile_pic_siswa)
    private val siswaName: TextView = view.findViewById(R.id.nama_siswa)
    private val kelas: TextView = view.findViewById(R.id.kelas)
//    private val comment: TextView = view.findViewById(R.id.comment)

    fun bindItem(item:Siswa, listener:(Siswa)->Unit){
        siswaName.text = item.nama
        kelas.text=item.kelas

        Glide.with(view)
            .asBitmap()
            .thumbnail(0.25f)
            .load(item.cover)
            .into(siswaPic)

        itemView.setOnClickListener{
            listener(item)
        }
    }
}


