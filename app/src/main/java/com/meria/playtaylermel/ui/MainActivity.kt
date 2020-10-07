package com.meria.playtaylermel.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.permissionMusic
import com.meria.playtaylermel.extensions.requestPermissionResultActivity
import com.meria.playtaylermel.ui.detail.DetailMusicActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val listMusic : ArrayList<String> = ArrayList()

    var musicAdapter : MusicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicAdapter = MusicAdapter()
        rvListMusic.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMusic.adapter = musicAdapter
        this.permissionMusic {
            initList()
        }
    }

    private fun initList(){
        val songs = getMusic(Environment.getExternalStorageDirectory())
        for (item in songs){
            listMusic.add(item.path)
        }
        musicAdapter?.list = listMusic
        musicAdapter?.onClickMusicSelected ={
           startActivity(DetailMusicActivity.newInstance(this,listMusic,it))
        }
        Log.d("listMusic","$listMusic")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      this.requestPermissionResultActivity(requestCode,grantResults){
          initList()
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
}