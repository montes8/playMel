package com.meria.playtaylermel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_music.view.*

class MusicAdapter (var onClickMusicSelected: ((String) -> Unit)? = null) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {



    var list: ArrayList<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clearListDataMedic(){
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.idNameMusic.text = model

    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}