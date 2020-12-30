package com.meria.playtaylermel.util

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.meria.playtaylermel.R
import com.meria.playtaylermel.model.AddressModel
import java.io.File

object Utils {
    fun toastGeneric(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    fun getImageBitmap(img : String):Bitmap?{
        val path = File(img)
        return BitmapFactory.decodeFile(path.absolutePath)
    }

    fun messagePrincess(id : Int,context: Context):String{
          return when(id){
            0->  context.resources.getString(R.string.message_welcome)
            1->  context.resources.getString(R.string.message_welcome_two)
            2->  context.resources.getString(R.string.message_welcome_three)
            3->  context.resources.getString(R.string.message_welcome_four)
              else -> context.resources.getString(R.string.message_welcome_four)
          }

    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses =
            am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

    fun reduceBitmapSize(imageFilePath: File): Bitmap {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath.absolutePath, bmOptions)
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions)
        bmOptions.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imageFilePath.absolutePath, bmOptions)
    }

    fun rotateIfNeeded(bitmap: Bitmap, uri: Uri, context: Context) : Bitmap {
        val exitInt = context.contentResolver.openInputStream(uri)?.let { ExifInterface(it) }
        return when(exitInt?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270)
            }
            else -> {
                bitmap
            }
        }
    }

    private fun calculateInSampleSize(bmOptions: BitmapFactory.Options): Int {
        val photoWidth = bmOptions.outWidth
        val photoHeight = bmOptions.outHeight
        var scaleFactor = 1
        if (photoWidth > 1000 || photoHeight > 1000) {
            val halfPhotoWidth = photoWidth / 2
            val halfPhotoHeight = photoHeight / 2
            while (halfPhotoWidth / scaleFactor >= 500 && halfPhotoHeight / scaleFactor >= 500) {
                scaleFactor *= 2
            }
        }
        return scaleFactor
    }

     fun listAddress():ArrayList<AddressModel>{
        val list : ArrayList<AddressModel> = ArrayList()
        list.add(AddressModel("Mega Plaza", "Alfreto Mendiola 3698,Independencia",-11.99405732, -77.06241231,"megaplaza"))
        list.add(AddressModel("Royal Plaza", "Av.Carlos izaguirre 287-289,Independencia", -11.99041933, -77.06290144,"megaplaza"))
        list.add(AddressModel("Plaza Norte", "Tomas Valle,Cercado de Lima 15311",-12.00681565, -77.05884285,"megaplaza"))
        list.add(AddressModel("Real Plaza", "Av.Inca Garcilaso de la Vega 1337,Cercado de Lima",-12.05688609, -77.03773475,"megaplaza"))
        list.add(AddressModel("Rambla", "Av.Brasil 702",-12.06631014, -77.04746379,"megaplaza"))
       return list
    }
}