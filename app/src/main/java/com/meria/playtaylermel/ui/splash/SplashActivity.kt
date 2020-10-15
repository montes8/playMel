package com.meria.playtaylermel.ui.splash

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.animationButton
import com.meria.playtaylermel.extensions.animationTop
import com.meria.playtaylermel.model.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import com.meria.playtaylermel.ui.home.MainActivity
import com.meria.playtaylermel.util.Utils.messagePrincess
import kotlinx.android.synthetic.main.activity_aplash.*
import java.util.*


class SplashActivity : AppCompatActivity(),TextToSpeech.OnInitListener {


    private  var textToSpeech : TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplash)
        textTitleSplash.animationTop()
        textSubTitleSplash.animationButton()
        initSplash()
        textToSpeech = TextToSpeech(this, this)
    }

    private fun convertTextToSpeech() {
        val text = resources.getString(R.string.message_welcome)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null,text)
    }

    private fun initSplash() {
        stopService(Intent(this, FloatingWidgetService::class.java))
        MusicTemporal.resetListMusic()
        val background = object : Thread() {
            override fun run() {
                        sleep((3000).toLong())
                startActivity(MainActivity.newInstance(this@SplashActivity))
                finish() }
        }
        background.start()
    }

    private fun getTimeFromAndroid() : Int{
        var messageId= 0
        val dt = Date()
        val hours = dt.hours
        if (hours >= 1 || hours <= 12) {
            messageId = 0

        } else if (hours >= 12 || hours <= 16) {
            messageId = 1
        } else if (hours >= 16 || hours <= 21) {
            messageId = 2
        } else if (hours >= 21 || hours <= 24) {
            messageId = 3
        }
        return messageId
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