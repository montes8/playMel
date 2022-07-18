package com.meria.playtaylermel.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.REQUEST_PERMISSION_READING_STATE
import com.meria.playtaylermel.util.TAG
import com.meria.playtaylermel.util.Utils.toastGeneric
import com.meria.playtaylermel.util.cantDelayButtonClick
import com.meria.playtaylermel.util.problemLog
import java.io.File
import java.util.concurrent.TimeUnit


fun Activity.permissionMusic(func: () -> Unit){
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_READING_STATE)
    }else{
        func()
    }
}

fun Activity.permissionLocation(){
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        Log.i(TAG, "sdk < 28 Q")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, strings, 1)
        }
    } else {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                "android.permission.ACCESS_BACKGROUND_LOCATION"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                "android.permission.ACCESS_BACKGROUND_LOCATION"
            )
            ActivityCompat.requestPermissions(this, strings, 2)
        }
    }

}

fun String.loadImageDetail(view: ImageView){
    if (this.isNotEmpty()){
        val path = File(this)
        val imgGallery = BitmapFactory.decodeFile(path.absolutePath)
        imgGallery?.let {
            view.setImageBitmap(it)
        }
    }
}

fun Activity?.showDialogCustom(resourceId: Int, func: Dialog.() -> Unit) {
    val dialog = Dialog(this ?: return)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(resourceId)
    dialog.func()
    dialog.show()
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}


fun View.flagViewState(flag : Boolean) {
    if (flag)this.visible() else this.gone()

}



fun TextView.animationTop(){
    val ani = AnimationUtils.loadAnimation(this.context, R.anim.top_in)
    this.animation=ani


}

fun View?.delayClickState(timeMillis: Long = 300) {
    this?.let {
        it.apply { it.isEnabled = false }
        Handler(Looper.getMainLooper()).postDelayed({
            it.apply { it.isEnabled = true }
        }, timeMillis)
    } ?: run {
        Log.d(problemLog, cantDelayButtonClick)
    }
}

fun TextView.animationButton(){
    val ani = AnimationUtils.loadAnimation(this.context, R.anim.button_out)
    this.animation=ani
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

fun fetchGalleryImages(context: Activity): ArrayList<String> {
    val galleryImageUrls: ArrayList<String> = ArrayList()
    val columns = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID
    ) //get all columns of type images
    val orderBy = MediaStore.Images.Media.DATE_TAKEN //order data by date
    val imagecursor = context.managedQuery(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
        null, "$orderBy DESC"
    ) //get all data in Cursor by sorting in DESC order
    for (i in 0 until imagecursor.count) {
        imagecursor.moveToPosition(i)
        val dataColumnIndex =
            imagecursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
        galleryImageUrls.add(imagecursor.getString(dataColumnIndex)) //get Image from column index
    }
    Log.e("fatch in", "images")
    return galleryImageUrls
}

























