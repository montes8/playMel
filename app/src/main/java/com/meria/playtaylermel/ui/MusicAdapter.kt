package com.meria.playtaylermel.ui

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.formatTimePlayer
import com.meria.playtaylermel.model.MusicModel
import kotlinx.android.synthetic.main.row_music.view.*

class MusicAdapter (var onClickMusicSelected: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    private var mPlayer: MediaPlayer? = null

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
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(model.path)
        mPlayer?.prepare()
        holder.itemView.idNameMusic.text = model.name
        holder.itemView.textTimeItem.formatTimePlayer(mPlayer?.duration?:0)
        holder.itemView.setOnClickListener {
            onClickMusicSelected?.invoke(position)
        }

    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}