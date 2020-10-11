package com.meria.playtaylermel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import kotlinx.android.synthetic.main.row_music.view.*

class MusicAdapter (var onClickMusicSelected: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    var list: ArrayList<MusicModel> = ArrayList()
        set(value) {
            field = value
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
        holder.itemView.idNameMusic.text = model.name
        holder.itemView.setOnClickListener {
            onClickMusicSelected?.invoke(position)
        }

    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}