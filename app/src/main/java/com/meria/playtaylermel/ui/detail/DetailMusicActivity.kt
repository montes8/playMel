package com.meria.playtaylermel.ui.detail

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.meria.playtaylermel.DATA_NAME_MUSIC
import com.meria.playtaylermel.DATA_POSITION_MUSIC
import com.meria.playtaylermel.R
import kotlinx.android.synthetic.main.activity_detail_music.*
import java.text.FieldPosition


class DetailMusicActivity : AppCompatActivity(), View.OnClickListener {

    var namesMusicList : ArrayList<String> = ArrayList()
    var positionMusic : Int = 0

    private var mPlayer: MediaPlayer? = null

    companion object {
        fun newInstance(context: Context, nameMusic: ArrayList<String>,position: Int): Intent {
            val intent = Intent(context, DetailMusicActivity::class.java)
            intent.putStringArrayListExtra(DATA_NAME_MUSIC, nameMusic)
            intent.putExtra(DATA_POSITION_MUSIC, position)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_music)
        intent?.let {
            namesMusicList= it.getStringArrayListExtra(DATA_NAME_MUSIC) as ArrayList<String>
            positionMusic = it.getIntExtra(DATA_POSITION_MUSIC,0)
        }
        initOnClick()
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(namesMusicList[positionMusic])
        mPlayer?.prepare();
        mPlayer?.start()
        initProgress()


    }

    private fun initOnClick(){
        imgFastRewind.setOnClickListener(this)
        imgSkipPrevious.setOnClickListener(this)
        imgPlay.setOnClickListener(this)
        imgSkipNext.setOnClickListener(this)
        imgFastForward.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id?:return){
            R.id.imgFastRewind->{}
            R.id.imgSkipPrevious->{}
            R.id.imgPlay->{}
            R.id.imgSkipNext->{}
            R.id.imgFastForward->{}

        }
    }

    private fun initProgress(){
        val background = object : Thread() {
            override fun run() {

                val duration = mPlayer?.duration?:0
                sbProgress.max = duration
                var positionCurrent = 0
                val ejecuction = 0
                val flag = false
                while (positionCurrent < duration){
                    try {
                        sleep((500).toLong())
                        positionCurrent = mPlayer?.currentPosition?:0
                        sbProgress.progress = positionCurrent

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }
        background.start()
    }
}