package com.meria.playtaylermel

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.extensions.permissionMusic
import com.meria.playtaylermel.extensions.requestPermissionResultActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {


    val listMusic : ArrayList<String> = ArrayList()

    var musicAdapter :MusicAdapter? = null

    val REQUEST_PERMISSION_READING_STATE = 12345

    private val RequieredPermission: String = android.Manifest.permission.READ_EXTERNAL_STORAGE

    @RequiresApi(Build.VERSION_CODES.R)
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
        val canciones = getMusic(Environment.getExternalStorageDirectory())
        for (item in canciones){
            listMusic.add(item.name.toString())
        }

        musicAdapter?.list = listMusic
        Log.d("listMusic","$listMusic")
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      this.requestPermissionResultActivity(requestCode,grantResults){
          initList()
      }
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