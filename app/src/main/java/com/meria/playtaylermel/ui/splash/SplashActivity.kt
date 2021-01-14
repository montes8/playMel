package com.meria.playtaylermel.ui.splash

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.common.ApiException
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.animationButton
import com.meria.playtaylermel.extensions.animationTop
import com.meria.playtaylermel.manager.IUpdateVersionManager
import com.meria.playtaylermel.model.temporal.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import com.meria.playtaylermel.ui.home.HomeActivity
import com.meria.playtaylermel.ui.manager.UpdateVersionManager
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import com.meria.playtaylermel.util.TAG
import me.leolin.shortcutbadger.ShortcutBadger

class SplashActivity : AppCompatActivity(), TextToSpeech.OnInitListener,IUpdateVersionManager {

    private var textToSpeech: TextToSpeech? = null
    private var updateVersionManager : UpdateVersionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        HiAnalyticsTools.enableLog()
        getToken()
        checkUpdate()
    }

    private fun getToken() {
        object : Thread() {
            override fun run() {
                try {
                    // read from agconnect-services.json
                    val appId =
                        AGConnectServicesConfig.fromContext(this@SplashActivity).getString("client/app_id")
                    val token = HmsInstanceId.getInstance(this@SplashActivity).getToken(appId, "HCM")
                    Log.d(TAG, "get token:$token")
                } catch (e: ApiException) {
                    Log.d(TAG, "get token failed, $e"
                    )
                }
            }
        }.start()
    }

    private fun convertTextToSpeech() {
        val text = resources.getString(R.string.message_welcome_twoo)
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

    override fun onResume() {
        super.onResume()
        clearAllNotifications()
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

    private fun clearAllNotifications(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        ShortcutBadger.removeCount(this)
    }


    override fun onShowUpdateDialog() {
       //not code
    }


}