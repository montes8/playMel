package com.meria.playtaylermel.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import com.meria.playtaylermel.R
import com.meria.playtaylermel.application.PlayApplication
import com.meria.playtaylermel.extensions.*
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.model.temporal.MusicTemporal
import com.meria.playtaylermel.repository.local.database.entity.ImageModel
import com.meria.playtaylermel.ui.detail.music.DetailMusicActivity
import com.meria.playtaylermel.ui.location.LocationActivity
import com.meria.playtaylermel.ui.map.MapsActivity
import com.meria.playtaylermel.ui.videos.VideosActivity
import com.meria.playtaylermel.util.Utils.isConnected
import com.meria.playtaylermel.util.Utils.toastGeneric
import kotlinx.android.synthetic.main.activity_home.*
import me.leolin.shortcutbadger.ShortcutBadger
import java.io.File
import kotlin.concurrent.thread


class HomeActivity : AppCompatActivity() {

    private val listMusic : ArrayList<MusicModel> = ArrayList()

    var musicAdapter : MusicAdapter? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        musicAdapter = MusicAdapter()
        ShortcutBadger.applyCount(this, 1)
        startImagesServiceWorker()


        rvListMusic.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMusic.adapter = musicAdapter
        this.permissionMusic {
            initList()
            this.permissionLocation()
        }

        floatingActionButton.setOnClickListener {
            if (isConnected(this)){
                startActivity(VideosActivity.newInstance(this))
            }else{
                toastGeneric(this,resources.getString(R.string.txt_error_internet))
            }

        }
        floatingActionButtonMap.setOnClickListener {
            if (isCastApiAvailable()){
                startActivity(MapsActivity.newInstance(this))
            }else{
                toastGeneric(this,"No tiene el hms")
            }

        }

        imgLocation.setOnClickListener {
            startActivity(LocationActivity.newInstance(this))
        }
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

    private fun startImagesServiceWorker() {
        Log.e("fatchin", "startImagesServiceWorker")
        Toast.makeText(applicationContext, "startImagesServiceWorker", Toast.LENGTH_SHORT).show()
        val images = fetchGalleryImages(this)
        thread(start = true) {
            images.forEach {
                PlayApplication.database?.musicDao()?.insertImage(
                    ImageModel(
                        id = 0,
                        path = it
                    )
                )
            }
        }
    }

}