package com.meria.playtaylermel.ui.detail.movie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.util.DATA_DATA_MOVIE
import com.meria.playtaylermel.util.EMPTY
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.mold_toolbar.*

class MovieActivity : AppCompatActivity() {

    private var modelMusic : MusicModel? = null
    private var urlHttp : String = EMPTY

    companion object {
        fun newInstance(context: Context, model : MusicModel): Intent {
            val intent =  Intent(context, MovieActivity::class.java)
            intent.putExtra(DATA_DATA_MOVIE,model)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        intent?.let {
            modelMusic = it.getParcelableExtra(DATA_DATA_MOVIE)
            urlHttp = modelMusic?.path?:EMPTY
            initMovie()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initMovie(){
        if (urlHttp.isNotEmpty()) {
            wvMovie.setInitialScale(30)
            wvMovie.setPadding(0, 0, 0, 0)
            val webSettings = wvMovie.settings
            webSettings.javaScriptEnabled = true
            wvMovie.webViewClient = object : WebViewClient() {}
            wvMovie.settings.setSupportZoom(true)
            wvMovie.settings.useWideViewPort = true
            wvMovie.settings.loadWithOverviewMode = true
            wvMovie.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            wvMovie.settings.builtInZoomControls = true
            wvMovie.settings.domStorageEnabled = true
            wvMovie.settings.displayZoomControls = false
            wvMovie.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            wvMovie.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            wvMovie.loadUrl(urlHttp)
        }
    }
}