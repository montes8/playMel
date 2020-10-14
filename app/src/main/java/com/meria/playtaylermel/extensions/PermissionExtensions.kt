package com.meria.playtaylermel.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.REQUEST_PERMISSION_READING_STATE
import com.meria.playtaylermel.util.Utils.toastGeneric
import com.meria.playtaylermel.util.cantDelayButtonClick
import com.meria.playtaylermel.util.problemLog
import java.util.concurrent.TimeUnit


fun Activity.permissionMusic(func: () -> Unit){
     val requiresPermission: String = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val permissionCheck = ContextCompat.checkSelfPermission(this, requiresPermission)
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiresPermission)) {
            func()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(requiresPermission),
                REQUEST_PERMISSION_READING_STATE
            )
        }
    } else {
        func()
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



fun TextView.animationTop(){
    val ani = AnimationUtils.loadAnimation(this.context, R.anim.top_in)
    this.animation=ani


}

fun View?.delayClickState(timeMillis: Long = 300) {
    this?.let {
        it.apply { it.isEnabled = false }
        Handler().postDelayed({
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

























