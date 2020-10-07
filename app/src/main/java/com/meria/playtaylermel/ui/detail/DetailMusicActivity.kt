package com.meria.playtaylermel.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meria.playtaylermel.DATA_NAME_MUSIC
import com.meria.playtaylermel.R

class DetailMusicActivity : AppCompatActivity() {

    var nameMusic : String? = null

    companion object {
        fun newInstance(context: Context, nameMusic: String?): Intent {
            val intent = Intent(context, DetailMusicActivity::class.java)
            intent.putExtra(DATA_NAME_MUSIC, nameMusic)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_music)

        intent?.let {
            it.getStringExtra(DATA_NAME_MUSIC)
        }
    }
}