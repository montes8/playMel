package com.meria.playtaylermel.ui.videos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.ui.detail.video.VideoDetailActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.row_videos.view.*


class VideosAdapter : RecyclerView.Adapter<VideosAdapter.MovieViewHolder>() {

    private val lifecycle: Lifecycle? = null

    init {
        setHasStableIds(true)
    }

    var list: ArrayList<MusicModel> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val youTubePlayerView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_videos, parent, false)
        return MovieViewHolder(youTubePlayerView)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.txtRowNameMusic.text = model.name
        lifecycle?.addObserver(holder.itemView.youtubePlayerView)
        holder.itemView.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = model.path
                videoId.let {
                    youTubePlayer.loadVideo(it, 0f)
                }
                if (model.play) {
                    youTubePlayer.play()
                    Log.d("positionlistPlay", "" + position)
                } else {
                    youTubePlayer.pause()
                    Log.d("positionlistPlay", "fals")
                }
            }
        })



        holder.itemView.imgZoom.setOnClickListener {
            holder.itemView.context.startActivity(
                VideoDetailActivity.newInstance(
                    holder.itemView.context,
                    model
                )
            )
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}