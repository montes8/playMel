package com.meria.playtaylermel.model.temporal

import android.media.MediaPlayer

object MediaPlayerSingleton : MediaPlayer(){


     var mediaPlayerSingleton   :MediaPlayer?=null

    fun setMediaPlayerSingleton(){
        mediaPlayerSingleton = MediaPlayer()
    }

    fun getInstanceMusic () =
        mediaPlayerSingleton

}