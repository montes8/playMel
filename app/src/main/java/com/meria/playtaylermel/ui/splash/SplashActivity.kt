package com.meria.playtaylermel.ui.splash

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.jos.AppUpdateClient
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.animationButton
import com.meria.playtaylermel.extensions.animationTop
import com.meria.playtaylermel.model.temporal.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import com.meria.playtaylermel.ui.home.MainActivity
import com.meria.playtaylermel.util.DEFAULT
import kotlinx.android.synthetic.main.activity_aplash.*
import java.util.*


class SplashActivity : AppCompatActivity(), TextToSpeech.OnInitListener,CheckUpdateCallBack {


    private var textToSpeech: TextToSpeech? = null

    var client: AppUpdateClient? = null
    var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplash)
        checkUpdate()
    }

    private fun convertTextToSpeech() {
        val text = resources.getString(R.string.message_welcome)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }

    private fun checkUpdate() {
        client = JosApps.getAppUpdateClient(this)
        client?.checkAppUpdate(this, this)
    }

    override fun onMarketStoreError(p0: Int) {
        // not cod
    }

    override fun onUpdateStoreError(p0: Int) {
        // not cod
    }

    override fun onUpdateInfo(intent: Intent?) {
        if (intent != null) {
            val status =
                intent.getIntExtra(UpdateKey.STATUS, DEFAULT)
            val rtnCode =
                intent.getIntExtra(UpdateKey.FAIL_CODE, DEFAULT)
            val rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON)
            val info = intent.getSerializableExtra(UpdateKey.INFO)
            if (flag){
                flag = false
                if (info is ApkUpgradeInfo) {
                    val upgradeInfo = info
                    checkUpdatePop(false, upgradeInfo )
                    finish()
                    Log.d("dataUpdate","There is a new update")
                }else{
                    initSplash()
                    Log.d("dataUpdate","onUpdateInfo status: $status, rtnCode: $rtnCode, rtnMessage: $rtnMessage")
                }
            }


        }
    }

    private fun checkUpdatePop(force: Boolean,apkUpgradeInfo : ApkUpgradeInfo) {
        client?.showUpdateDialog(this, apkUpgradeInfo, force)
        Log.d("dataUpdateForc", "checkUpdatePop success")
    }


    override fun onMarketInstallInfo(p0: Intent?) {
        // not cod
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
                startActivity(MainActivity.newInstance(this@SplashActivity))
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


}