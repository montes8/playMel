package com.meria.playtaylermel.ui.gallery.detail

import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.DATA_IMG
import com.meria.playtaylermel.util.EMPTY
import kotlinx.android.synthetic.main.dialog_detail_image.*
import java.io.File


class DetailImageDialogFragment : DialogFragment(){

    private var pathUrl = EMPTY

    companion object {
        fun newInstance(url : String): DetailImageDialogFragment {
            val frag = DetailImageDialogFragment()
            val args = Bundle()
            args.putString(DATA_IMG, url)

            frag.arguments = args
            return  frag
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val backgroundColor = context?.let {  ContextCompat.getColor(it, R.color.transparent)}?.let { ColorDrawable(it) }
          dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(backgroundColor)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_detail_image, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            pathUrl = it.getString(DATA_IMG)?: EMPTY
        }
        contentImgReport.setOnClickListener {
            dismiss()
        }
        if (pathUrl.isNotEmpty()){
            val path = File(pathUrl)
            val imgGallery = BitmapFactory.decodeFile(path.absolutePath)
            imgGallery?.let {
                imgDetailReport.setImageBitmap(it)
            }
        }

    }
}
