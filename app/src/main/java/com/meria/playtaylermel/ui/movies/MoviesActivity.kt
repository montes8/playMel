package com.meria.playtaylermel.ui.movies

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.ui.detail.movie.MovieActivity
import com.meria.playtaylermel.ui.videos.VideosActivity
import com.meria.playtaylermel.ui.videos.VideosAdapter
import com.meria.playtaylermel.util.DATA_DATA_MOVIE
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.activity_videos.*
import kotlinx.android.synthetic.main.mold_toolbar.*

class MoviesActivity : AppCompatActivity() {


    private var movieAdapter : MoviesAdapter? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return  Intent(context, MoviesActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        constraintLayout.setBackgroundColor(resources.getColor(R.color.white,null))
        txtToolbar.text = getString(R.string.txt_toolbar_movie)
        txtToolbar.setTextColor(resources.getColor(R.color.colorAccent,null))
        movieAdapter = MoviesAdapter()
        rvMovies.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMovies.adapter = movieAdapter
        val listMovie : ArrayList<MusicModel> = ArrayList()
        listMovie.add(MusicModel(1,"Frozen: Una Aventura Congelada","https://pelisplus.me/pelicula/frozen-una-aventura-congelada/",false))
        listMovie.add(MusicModel(2,"Frozen: Fiebre Congelada","https://pelisplus.me/pelicula/frozen-fiebre-congelada/",false))
        listMovie.add(MusicModel(3,"Frozen: Una Aventura de Olaf","https://pelisplus.me/pelicula/frozen-una-aventura-de-olaf",true))
        listMovie.add(MusicModel(4,"Enredados","https://pelisplus.me/pelicula/enredados",true))
        listMovie.add(MusicModel(10,"Valiente","https://pelisplus.me/pelicula/valiente/",true))
        listMovie.add(MusicModel(5,"Enredados Otra Vez","https://pelisplus.me/pelicula/enredados-otra-vez",true))
        listMovie.add(MusicModel(6,"La Sirenita","https://pelisplus.me/pelicula/la-sirenita",true))
        listMovie.add(MusicModel(7,"La Sirenita 2: Regreso al Mar","https://pelisplus.me/pelicula/la-sirenita-2-regreso-al-mar",true))
        listMovie.add(MusicModel(8,"La Sirenita 3: El Origen de la Sirenita","https://pelisplus.me/pelicula/la-sirenita-3-el-origen-de-la-sirenita",true))
        listMovie.add(MusicModel(9,"La Princesa Encantada","https://pelisplus.me/pelicula/la-princesa-encantada",true))
        listMovie.add(MusicModel(10,"La Cenicienta ","https://pelisplus.me/pelicula/cenicienta-3-que-pasaria-si",true))
        listMovie.add(MusicModel(11,"Tinker Bell y El Tesoro Perdido","https://pelisplus.me/pelicula/tinker-bell-y-el-tesoro-perdido",true))
        listMovie.add(MusicModel(12,"La Cenicienta y El Príncipe Oculto","https://pelisplus.me/pelicula/la-cenicienta-y-el-principe-oculto",true))
        listMovie.add(MusicModel(13,"Peter Pan","https://pelisplus.me/pelicula/peter-pan-1953",true))
        listMovie.add(MusicModel(14,"El Libro de la Selva","https://pelisplus.me/pelicula/el-libro-de-la-selva",true))
        listMovie.add(MusicModel(15,"El Rey León 2: El Tesoro de Simba","https://pelisplus.me/pelicula/el-rey-leon-2-el-tesoro-de-simba",true))
        listMovie.add(MusicModel(10,"Madagascar","https://pelisplus.me/pelicula/madagascar",true))
        movieAdapter?.list = listMovie

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(VideosActivity.newInstance(this))
    }
}