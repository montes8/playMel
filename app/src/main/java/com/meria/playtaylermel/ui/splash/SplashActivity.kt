package com.meria.playtaylermel.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.formatTimePlayer
import com.meria.playtaylermel.ui.MainActivity
import kotlinx.android.synthetic.main.activity_detail_music.*

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplash)
        initSplash()
    }

    private fun initSplash() {
        val background = object : Thread() {
            override fun run() {
                        sleep((5000).toLong())
                startActivity(MainActivity.newInstance(this@SplashActivity))
            }
        }
        background.start()
    }
}