package com.meria.playtaylermel.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.row_movies.view.*


class MovieAdapter (var onClickMusicSelected: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val lifecycle: Lifecycle? = null

    var list: ArrayList<MusicModel> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val youTubePlayerView = LayoutInflater.from(parent.context).inflate(R.layout.row_movies, parent, false) as YouTubePlayerView
        lifecycle?.addObserver(youTubePlayerView)
        return MovieViewHolder(youTubePlayerView)

    }



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = model.path
                videoId.let { youTubePlayer.loadVideo(it, 0f) }
                youTubePlayer.pause()
            }
        })
    }

    class MovieViewHolder(itemView: YouTubePlayerView) : RecyclerView.ViewHolder(itemView)
}