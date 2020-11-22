package com.meria.playtaylermel.ui.videos

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
import com.meria.playtaylermel.ui.movies.MoviesActivity
import kotlinx.android.synthetic.main.activity_videos.*
import kotlinx.android.synthetic.main.mold_toolbar.*
import java.util.*
import kotlin.collections.ArrayList


class VideosActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var movieAdapter : VideosAdapter? = null
    private  var textToSpeech : TextToSpeech? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, VideosActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        txtToolbar.text = getString(R.string.txt_toolbar_movi)
        textToSpeech = TextToSpeech(this, this)
        movieAdapter = VideosAdapter()
        rvListMovies.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMovies.adapter = movieAdapter
        val listMovie : ArrayList<MusicModel> = ArrayList()
        listMovie.add(MusicModel(1,"Libre Soy (De Frozen: Una Aventura Congelada Con letra)","rVjI9YRc4pE",true))
        listMovie.add(MusicModel(2,"AURORA - Mucho Más Allá","VxBtzRiWBsY",false))
        listMovie.add(MusicModel(3,"¿Y si hacemos un muñeco? - Disney Frozen","n6TK7lcS_ow",false))
        listMovie.add(MusicModel(4,"Leslie Gil - Muéstrate","jcllZ4jSIGI",false))
        listMovie.add(MusicModel(5,"Cuándo empezaré a vivir | Enredados","NjkAgGwXaSs",false))
        listMovie.add(MusicModel(6,"Veo en ti la Luz","tDBFyH7Al-c",false))
        listMovie.add(MusicModel(7,"Frozen 2 - Elsa y Anna encuentran el barco de sus padres","vTYr8S4sBLs",false))
        listMovie.add(MusicModel(10,"Las Fiestas al Fin (De “Olaf: Otra Aventura Congelada de Frozen\")","U7-4pPl0zRM",false))
        movieAdapter?.list = listMovie
        floatingMovies.setOnClickListener {
            startActivity(MoviesActivity.newInstance(this))
            movieAdapter?.removeLifecycle()
            finish()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        movieAdapter?.removeLifecycle()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieAdapter?.removeLifecycle()
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