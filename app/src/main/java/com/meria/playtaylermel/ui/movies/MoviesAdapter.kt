package com.meria.playtaylermel.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.ui.detail.movie.MovieActivity
import kotlinx.android.synthetic.main.row_movie.view.*

class MoviesAdapter  : RecyclerView.Adapter<MoviesAdapter.MusicViewHolder>() {

    var list: ArrayList<MusicModel> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_movie, parent, false)
        return MusicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.idNameMusicTwo.text = model.name
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(MovieActivity.newInstance( holder.itemView.context,model))
        }

    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}