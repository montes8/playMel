package com.meria.playtaylermel

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {


    val listMusic : ArrayList<String> = ArrayList()

    var musicAdapter :MusicAdapter? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val canciones = getMusic(File(Environment.getRootDirectory().path))
        for (item in canciones){
            listMusic.add(item.name.toString())
        }

        musicAdapter = MusicAdapter()
        rvListMusic.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvListMusic.adapter = musicAdapter
        musicAdapter?.list = listMusic
        Log.d("listMusic","$listMusic")


    }



    private fun getMusic(root: File): ArrayList<File> {
        val canciones: ArrayList<File> = ArrayList()
        val archivos = root.listFiles()
        archivos?.let {
            for (item in it) {
                if (item.isDirectory && !item.isHidden) {
                    canciones.addAll(getMusic(item))
               } else {
                    if (item.name.endsWith(".mp3")) {
                        canciones.add(item)
                    }
                }
            }
        }
        return canciones
    }
}