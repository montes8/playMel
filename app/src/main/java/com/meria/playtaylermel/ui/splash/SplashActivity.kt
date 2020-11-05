package com.meria.playtaylermel.ui.splash

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.animationButton
import com.meria.playtaylermel.extensions.animationTop
import com.meria.playtaylermel.manager.IUpdateVersionManager
import com.meria.playtaylermel.model.temporal.MusicTemporal
import com.meria.playtaylermel.ui.manager.UpdateVersionManager
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import com.meria.playtaylermel.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : AppCompatActivity(), TextToSpeech.OnInitListener,IUpdateVersionManager {

    private var textToSpeech: TextToSpeech? = null
    private var updateVersionManager : UpdateVersionManager? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkUpdate()
    }


    private fun convertTextToSpeech() {
        val text = resources.getString(R.string.message_welcome)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }

    private fun checkUpdate() {
        updateVersionManager =
            UpdateVersionManager(this, this)
        updateVersionManager?.checkAppUpdate()
    }

    private fun initSplash() {
        stopService(Intent(this, FloatingWidgetService::class.java))
        textTitleSplash.animationTop()
        textSubTitleSplash.animationButton()
        textToSpeech = TextToSpeech(this, this)
        MusicTemporal.resetListMusic()
        val background = object : Thread() {
            override fun run() {
                sleep((3000).toLong())
                startActivity(HomeActivity.newInstance(this@SplashActivity))
                finish()
            }
        }
        background.start()
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

    override fun onNotUpdateDialog() {
        initSplash()
    }

    override fun onShowUpdateDialog() {
       //not code
    }


}