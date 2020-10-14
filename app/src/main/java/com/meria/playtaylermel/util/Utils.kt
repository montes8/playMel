package com.meria.playtaylermel.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.meria.playtaylermel.R
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

    fun messagePrincess(id : Int,context: Context):String{
          return when(id){
            0->  context.resources.getString(R.string.message_welcome)
            1->  context.resources.getString(R.string.message_welcome_two)
            2->  context.resources.getString(R.string.message_welcome_three)
            3->  context.resources.getString(R.string.message_welcome_four)
              else -> context.resources.getString(R.string.message_welcome_four)
          }

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
}