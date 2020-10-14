package com.meria.playtaylermel.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.Utils.isConnected
import com.meria.playtaylermel.util.Utils.toastGeneric
import com.meria.playtaylermel.extensions.permissionMusic
import com.meria.playtaylermel.extensions.requestPermissionResultActivity
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.model.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.DetailMusicActivity
import com.meria.playtaylermel.ui.movies.MoviesActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val listMusic : ArrayList<MusicModel> = ArrayList()

    var musicAdapter : MusicAdapter? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicAdapter = MusicAdapter()
        rvListMusic.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMusic.adapter = musicAdapter
        this.permissionMusic {
            initList()
        }

        floatingActionButton.setOnClickListener {
            if (isConnected(this)){
                startActivity(MoviesActivity.newInstance(this))
            }else{
                toastGeneric(this,resources.getString(R.string.txt_error_internet))
            }

        }
    }


    private fun initList(){
        val songs = getMusic(Environment.getExternalStorageDirectory())
        for (item in songs){
            listMusic.add(MusicModel(name = item.name,path = item.path))
        }
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
}