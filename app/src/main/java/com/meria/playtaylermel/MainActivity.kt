package com.meria.playtaylermel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    private fun getMusic(root: File): ArrayList<File> {
        val canciones: ArrayList<File> = ArrayList()
        val archivos = root.listFiles()
        archivos?.let {
            for (item in it) {
                if (item.isDirectory && !item.isHidden) {
                    canciones.addAll(getMusic(item))
                } else {
                    if (item.name.endsWith(".mp3") || item.name.endsWith(".vav")) {
                        canciones.add(item)
                    }
                }
            }
        }
        return canciones
    }
}