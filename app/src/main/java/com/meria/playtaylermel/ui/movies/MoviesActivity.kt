package com.meria.playtaylermel.ui.movies

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity() {

    var movieAdapter : MovieAdapter? = null


    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MoviesActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        movieAdapter = MovieAdapter()
        rvListMovies.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMovies.adapter = movieAdapter
        val listMovie : ArrayList<MusicModel> = ArrayList()
        listMovie.add(MusicModel("Libre Soy (De Frozen: Una Aventura Congelada Con letra)","rVjI9YRc4pE"))
        listMovie.add(MusicModel("AURORA - Mucho Más Allá","VxBtzRiWBsY"))
        listMovie.add(MusicModel("¿Y si hacemos un muñeco? - Disney Frozen","n6TK7lcS_ow"))
        listMovie.add(MusicModel("Leslie Gil - Muéstrate","jcllZ4jSIGI"))
         movieAdapter?.list = listMovie


    }
}