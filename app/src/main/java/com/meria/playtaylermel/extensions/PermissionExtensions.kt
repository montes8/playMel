package com.meria.playtaylermel.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.R
import com.meria.playtaylermel.REQUEST_PERMISSION_READING_STATE
import com.meria.playtaylermel.Utils.toastGeneric
import java.util.concurrent.TimeUnit


fun Activity.permissionMusic(func: () -> Unit){
     val requiresPermission: String = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val permissionCheck = ContextCompat.checkSelfPermission(this, requiresPermission)
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiresPermission)) {
            func()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(requiresPermission), REQUEST_PERMISSION_READING_STATE)
        }
    } else {
        func()
    }

}

@SuppressLint("DefaultLocale")
fun TextView.formatTimePlayer(time :Int){
    val timeFinal = java.lang.String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(time.toLong()),
        TimeUnit.MILLISECONDS.toSeconds(time.toLong()) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                time.toLong()))
    )
    this.text = timeFinal

}

fun Activity.requestPermissionResultActivity(requestCode: Int, grantResults: IntArray,func: () -> Unit){
    when (requestCode) {
        REQUEST_PERMISSION_READING_STATE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            func()
        } else {
            toastGeneric(this,this.resources.getString(R.string.error_message_music_denied))
        }
    }

}

























