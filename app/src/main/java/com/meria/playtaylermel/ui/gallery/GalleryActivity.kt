package com.meria.playtaylermel.ui.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.meria.playtaylermel.R
import com.meria.playtaylermel.application.PlayApplication
import com.meria.playtaylermel.extensions.showDialogCustom
import com.meria.playtaylermel.repository.database.entity.ImageModel
import com.meria.playtaylermel.ui.gallery.detail.DetailImageDialogFragment
import com.meria.playtaylermel.util.CameraController
import com.meria.playtaylermel.util.Utils.getImageBitmap
import com.meria.playtaylermel.util.Utils.toastGeneric
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.dialog_delete_image.*
import kotlinx.android.synthetic.main.mold_toolbar.*
import kotlin.concurrent.thread

class GalleryActivity : AppCompatActivity(), CameraController.CameraControllerListener {

    var handler: Handler = Handler()
    private var cameraManager: CameraController? = null

    private var adapter: ImageAdapter? = null
    private var imagesList: ArrayList<ImageModel> = ArrayList()



    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, GalleryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        txtToolbar.text = getString(R.string.txt_toolbar_gallery)
        initListImages()

    }

    private fun initListImages() {
        cameraManager = CameraController(this, this)
        rvImages.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter()
        rvImages.adapter = adapter
        thread(start = true) {
            imagesList.add(
                ImageModel(
                    id = 0,
                    path = ""
                )
            )
            val imagesListUpdate =
                PlayApplication.database?.musicDao()?.getListImages() ?: ArrayList()
            if (imagesListUpdate.isNotEmpty()) {
                imagesList.addAll(imagesListUpdate)
            }
            handler.post {
                addListImageList()
            }
        }
    }


    override fun onCameraPermissionDenied() {
        toastGeneric(this, getString(R.string.error_message_music_denied))
    }

    private fun addListImageList() {
        adapter?.imagesList = imagesList
        adapter?.onClick = {
            onCameraClick()
        }
        adapter?.onClickDelete = {
            dialogDeleteImage(it)
        }
        adapter?.onClickDetail = { url ->
               if (url.path.isNotEmpty()) {
                   val imgUrl = getImageBitmap(url.path)
                   imgUrl.let {
                       DetailImageDialogFragment.newInstance(url.path).show(supportFragmentManager, DetailImageDialogFragment::class.java.name)
                   }
               }
        }

    }

    private fun dialogDeleteImage(position: Int) {
        this@GalleryActivity.showDialogCustom(R.layout.dialog_delete_image) {
            val backgroundColor = ColorDrawable(
                ContextCompat.getColor(this@GalleryActivity, android.R.color.transparent)
            )
            this.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.window?.setBackgroundDrawable(backgroundColor)
            this.btnAcceptDialog.setOnClickListener {
                thread(start = true) {
                    val entity =
                        PlayApplication.database?.musicDao()?.getImageId(imagesList[position].id)
                    entity?.let {
                        PlayApplication.database?.musicDao()?.deleteImage(it)
                        handler.post {
                            adapter?.removeItem(position)
                        }
                    }
                }



                this.dismiss()
            }

            this.btnCancelDialog.setOnClickListener {
                this.dismiss()
            }
            imgCloseLogout.setOnClickListener {
                this.dismiss()
            }

        }
    }

    private fun onCameraClick() {
        cameraManager?.doCamera()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraManager?.onActivityResult(requestCode,resultCode,data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraManager?.onRequestPermissionsResult(requestCode,grantResults)
    }


    override fun onGetImageCameraCompleted(path: String, img: Bitmap) {
        thread(start = true) {
            imagesList.clear()
            PlayApplication.database?.musicDao()?.insertImage(
                ImageModel(
                    id = 0,
                    path = path
                )
            )
            imagesList.add(
                ImageModel(
                    path = "",
                    id = 0
                )
            )
            val imagesListUpdate =
                PlayApplication.database?.musicDao()?.getListImages() ?: ArrayList()
            imagesList.addAll(imagesListUpdate)
            handler.post {
                adapter?.imagesList = imagesList
            }
        }

        Log.d("imagesListData", "$imagesList")
    }
}