package com.meria.playtaylermel.util

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.Utils.reduceBitmapSize
import com.meria.playtaylermel.util.Utils.rotateIfNeeded
import java.io.*
import java.util.*

class CameraController (private val context: Context,private val listener: CameraControllerListener?){

    private var pictureFileName = EMPTY
    private lateinit var picturePathTemp: String
    private lateinit var pictureNameTemp: String



    fun doCamera(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
            return
        }
        var pictureFile: File? = null
        try {
            pictureFile = createPictureFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (pictureFile != null) {
            val pictureUri = FileProvider.getUriForFile(context, context.applicationContext.packageName, pictureFile)
            chooseCameraOptions( context as Activity, context.getString(R.string.Select_option),
                pictureUri
            )
        }
    }



    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                doCamera()
            else
                listener?.onCameraPermissionDenied()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CAMERA_PERMISSION && resultCode == Activity.RESULT_OK) {
            val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val storageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "" + "imageMusic")
            if(data == null) {
                putImageCamera(storageDir,externalFilesDir)
            } else {
                putImageIntoFolder(data,externalFilesDir,storageDir)
            }
        }
    }


    private fun putImageCamera(storageDir : File,externalFilesDir :String){
        val directory = File(storageDir.path)
        val files = directory.listFiles()
        if(files != null) {
            for (i in files.indices) {
                val imgFile = File(storageDir.path + "/" + files[i].name)
                    if(imgFile.name == pictureNameTemp && imgFile.exists()) {
                        val myBitmap = rotateIfNeeded(reduceBitmapSize(imgFile),Uri.fromFile(imgFile),context)
                        try {
                            FileOutputStream(picturePathTemp).use { out -> myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out) }
                            val path = "$externalFilesDir/imageMusic/$pictureFileName.jpg"
                            val file = File(path)
                            val imgGallery = BitmapFactory.decodeFile(file.absolutePath)
                            listener?.onGetImageCameraCompleted(path,rotateIfNeeded(imgGallery,Uri.fromFile(file),context))
                        } catch (e: IOException) {
                            e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun putImageIntoFolder(data: Intent?, externalFilesDir: String, storageDir: File) {
        try {
            val calendar = Calendar.getInstance()
            val pictureFileName = calendar.timeInMillis.toString()
            val photoFile = File(storageDir.path + "/" + pictureFileName + ".jpg")
            val inputStream: InputStream? = data?.data?.let { context.contentResolver.openInputStream(it) }
            val fOutputStream: FileOutputStream? = FileOutputStream(photoFile)
            copyStream(inputStream, fOutputStream)
            fOutputStream?.close()
            inputStream?.close()
            val bitmap = rotateIfNeeded(reduceBitmapSize(photoFile),Uri.fromFile(photoFile),context)
            FileOutputStream(photoFile).use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out) }
            val path = "$externalFilesDir/imageMusic/$pictureFileName.jpg"
            val file = File(path)
            val imgGallery = BitmapFactory.decodeFile(file.absolutePath)

             listener?.onGetImageCameraCompleted(path,imgGallery)
        } catch (e: java.lang.Exception) {
            Log.d("PHOTO_2ND_ERROR", "onActivityResult: $e")
        }
    }

    @Throws(IOException::class)
    private fun copyStream(input: InputStream?, output: OutputStream?) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        do {
            input?.apply {
                bytesRead = this.read(buffer)
                if(bytesRead == -1)
                    return
                output?.write(buffer, 0, bytesRead)
            }
        } while (true)
    }

    private fun createPictureFile(): File {
        val calendar = Calendar.getInstance()
        pictureFileName = calendar.timeInMillis.toString()
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val picture = File("$storageDir/imageMusic", "$pictureFileName.jpg")
        val newPath = File("$storageDir/imageMusic")
        if(!newPath.exists()) {
            newPath.mkdirs()
        }
        picturePathTemp = picture.absolutePath
        pictureNameTemp = picture.name
        return picture
    }

    private fun chooseCameraOptions(context: Activity, title: String, outputFileUri: Uri) {
        val cameraIntents = ArrayList<Intent>()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = context.packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            cameraIntents.add(intent)
        }
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent.createChooser(galleryIntent, title)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray<Parcelable>())
        context.startActivityForResult(chooserIntent, REQUEST_CAMERA_PERMISSION)
    }



    interface CameraControllerListener {
        fun onCameraPermissionDenied()
        fun onGetImageCameraCompleted(path : String,img : Bitmap)
    }
}