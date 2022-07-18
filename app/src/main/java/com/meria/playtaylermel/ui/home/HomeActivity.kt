package com.meria.playtaylermel.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HAEventType.SUBMITSCORE
import com.huawei.hms.analytics.type.HAParamType.SCORE
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.*
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.model.temporal.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.DetailMusicActivity
import com.meria.playtaylermel.ui.location.LocationActivity
import com.meria.playtaylermel.ui.map.MapsActivity
import com.meria.playtaylermel.ui.videos.VideosActivity
import com.meria.playtaylermel.util.Utils.isConnected
import com.meria.playtaylermel.util.Utils.toastGeneric
import kotlinx.android.synthetic.main.activity_home.*
import me.leolin.shortcutbadger.ShortcutBadger
import java.io.File


class HomeActivity : AppCompatActivity() {

    private val listMusic : ArrayList<MusicModel> = ArrayList()

    var musicAdapter : MusicAdapter? = null
    var instance: HiAnalyticsInstance? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        instance = HiAnalytics.getInstance(this)
        musicAdapter = MusicAdapter()
        ShortcutBadger.applyCount(this, 1)


        rvListMusic.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMusic.adapter = musicAdapter
        this.permissionMusic {
            initList()
            this.permissionLocation()
        }

        floatingActionButton.setOnClickListener {
            if (isConnected(this)){
                startActivity(VideosActivity.newInstance(this))
                reportAnswerEvt("siguiente videos","pantalla principal")
            }else{
                toastGeneric(this,resources.getString(R.string.txt_error_internet))
            }

        }
        floatingActionButtonMap.setOnClickListener {
            if (isCastApiAvailable()){
                startActivity(MapsActivity.newInstance(this))
                reportAnswerEvt("siguiente mapas","pantalla principal")
            }else{
                toastGeneric(this,"No tiene el hms")
            }

        }

        imgLocation.setOnClickListener {
            startActivity(LocationActivity.newInstance(this))
        }
        Log.d("imageneslocal","${fetchGalleryImages(this)}")
    }

    private fun isCastApiAvailable(): Boolean {
        return (isConnected(this)
                && HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(this) == ConnectionResult.SUCCESS)
    }


    private fun initList(){
        pgLoadingFragment.show()
        pgLoadingFragment.visible()
        val songs = getMusic(Environment.getExternalStorageDirectory())
        for (item in songs){
            listMusic.add(MusicModel(name = item.name,path = item.path))
        }
        pgLoadingFragment.hide()
        pgLoadingFragment.gone()
        musicAdapter?.list = listMusic
        musicAdapter?.onClickMusicSelected ={
            MusicTemporal.addListMusic(listMusic)
            MusicTemporal.setPositionMusic(it)
            reportAnswerEvt("siguiente detail musica","pantalla principal")
            startActivityForResult(DetailMusicActivity.newInstance(this),120)
        }
        Log.d("listMusic","$listMusic")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      this.requestPermissionResultActivity(requestCode,grantResults){
          initList()
          this.permissionLocation()
      }
    }

    private fun getMusic(root: File): ArrayList<File> {
        val filesMusic: ArrayList<File> = ArrayList()
        val files = root.listFiles()
        files?.let {
            for (item in it) {
                if (item.isDirectory && !item.isHidden) {
                    filesMusic.addAll(getMusic(item))
               } else {
                    if (item.name.endsWith(".mp3")) {
                        filesMusic.add(item)
                    }
                }
            }
        }
        return filesMusic
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 120) {
            finish()
        }
    }

    private fun reportAnswerEvt(nav: String,title : String) {
        val bundle = Bundle()
        bundle.putString("pantalla", title)
        bundle.putString("navegacion", nav)


        // Report a preddefined Event
        instance?.onEvent("Answer", bundle)
    }

}