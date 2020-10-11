package com.meria.playtaylermel.ui.splash

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.animationButton
import com.meria.playtaylermel.extensions.animationTop
import com.meria.playtaylermel.ui.home.MainActivity
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
        val text = getString(R.string.message_welcome)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null,text)
    }

    private fun initSplash() {
        val background = object : Thread() {
            override fun run() {
                        sleep((3000).toLong())
                startActivity(MainActivity.newInstance(this@SplashActivity))
                finish() }
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
}