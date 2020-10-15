package com.meria.playtaylermel.ui.gallery

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.delayClickState
import com.meria.playtaylermel.extensions.gone
import com.meria.playtaylermel.model.ImageModel
import kotlinx.android.synthetic.main.row_image.view.*
import java.io.File

class ImageAdapter(var onClick: ((ImageModel) -> Unit)? = null, var onClickDelete: ((Int) -> Unit)? = null,
                   var onClickDetail: ((ImageModel) -> Unit)? = null) : RecyclerView.Adapter<ImageHolder>() {

    init {
        setHasStableIds(true)
    }

     var imagesList: ArrayList<ImageModel> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun removeItem(position: Int) {
        val path = File(imagesList[position].path)
        path.delete()
        imagesList.removeAt(position)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageHolder {
        return ImageHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.row_image, parent, false)
        )
    }

    override fun getItemCount(): Int {
        val checkedUser = imagesList
        return checkedUser.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val img = imagesList[position]
        holder.itemView.cardGallery.setOnClickListener {
            holder.itemView.cardGallery.delayClickState()
            if (position == 0) {
                onClick?.invoke(img)
            }else{
                onClickDetail?.invoke(img)
            }
        }


        if (position == 0) {
            holder.itemView.imgDeletePhoto.gone()
            holder.itemView.imgGallery.scaleType = ImageView.ScaleType.CENTER
        }

        if (position != 0) {
            if (img.path.isNotEmpty()){
                val path = File(img.path)
                val imgGallery = BitmapFactory.decodeFile(path.absolutePath)
                imgGallery?.let {
                    holder.itemView.imgGallery.setImageBitmap(it)
                }
            }
        }

        holder.itemView.imgDeletePhoto.setOnClickListener {
            holder.itemView.imgDeletePhoto.delayClickState()
            onClickDelete?.invoke(position)}
    }
}


class ImageHolder(view: View) : RecyclerView.ViewHolder(view)