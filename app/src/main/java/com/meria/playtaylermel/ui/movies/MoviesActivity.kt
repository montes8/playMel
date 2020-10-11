package com.meria.playtaylermel.ui.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import kotlinx.android.synthetic.main.activity_movies.*
import java.util.*
import kotlin.collections.ArrayList


class MoviesActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var movieAdapter : MovieAdapter? = null
    private  var textToSpeech : TextToSpeech? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MoviesActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        textToSpeech = TextToSpeech(this, this)
        movieAdapter = MovieAdapter()
        rvListMovies.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMovies.adapter = movieAdapter
        val listMovie : ArrayList<MusicModel> = ArrayList()
        listMovie.add(MusicModel(1,"Libre Soy (De Frozen: Una Aventura Congelada Con letra)","rVjI9YRc4pE",false))
        listMovie.add(MusicModel(2,"AURORA - Mucho Más Allá","VxBtzRiWBsY",false))
        listMovie.add(MusicModel(3,"¿Y si hacemos un muñeco? - Disney Frozen","n6TK7lcS_ow",false))
        listMovie.add(MusicModel(4,"Leslie Gil - Muéstrate","jcllZ4jSIGI",false))
        movieAdapter?.list = listMovie
    }

    private fun convertTextToSpeech() {
        val text = getString(R.string.message_movie)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null,text)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val loc = Locale("es", "ES")
            val result = textToSpeech?.setLanguage(loc)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("error", "This Language is not supported")
            } else {
                convertTextToSpeech()
            }
        } else {
            Log.d("error", "Initilization Failed!")
        }
    }
}