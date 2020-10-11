package com.meria.playtaylermel.ui.detail.movie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import com.meria.playtaylermel.DATA_DATA_MUSIC
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private var modelMusic : MusicModel? = null

    companion object {
        fun newInstance(context: Context,model : MusicModel): Intent {
            val intent =  Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(DATA_DATA_MUSIC,model)

            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        modelMusic = intent.getParcelableExtra(DATA_DATA_MUSIC)
        lifecycle.addObserver(youtubePlayerLandScape)
        youtubePlayerLandScape.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = modelMusic?.path?:"0"
                videoId.let { youTubePlayer.loadVideo(it, 0f) }
                youTubePlayer.play()
            }
        })
    }
}