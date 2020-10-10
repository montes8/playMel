package com.meria.playtaylermel.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.DATA_NAME_MUSIC
import com.meria.playtaylermel.DATA_POSITION_MUSIC
import com.meria.playtaylermel.R
import kotlinx.android.synthetic.main.activity_detail_music.*
import java.util.concurrent.TimeUnit


class DetailMusicActivity : AppCompatActivity(), View.OnClickListener {

    var namesMusicList: ArrayList<String> = ArrayList()
    var positionMusic: Int = 0

    private var mPlayer: MediaPlayer? = null
    private var aux: String? = null

    companion object {
        fun newInstance(context: Context, nameMusic: ArrayList<String>, position: Int): Intent {
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
            namesMusicList = it.getStringArrayListExtra(DATA_NAME_MUSIC) as ArrayList<String>
            positionMusic = it.getIntExtra(DATA_POSITION_MUSIC, 0)
        }
        sbProgress.setOnSeekBarChangeListener(object  : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mPlayer?.seekTo(sbProgress.progress)
            }

        })
        initOnClick()
        playMusic(namesMusicList[positionMusic])


    }

    override fun onBackPressed() {
        mPlayer?.stop()
        super.onBackPressed()

    }

    @SuppressLint("DefaultLocale")
    private fun playMusic(music: String) {
        mPlayer?.let {
            it.stop()
        }
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(music)
        mPlayer?.prepare();
        mPlayer?.start()
        initProgress()
        val durationFinal = mPlayer?.duration?:0
        val timeFinal = java.lang.String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(durationFinal.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(durationFinal.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
                durationFinal.toLong()))
        )
        txtDurationFinal.text = timeFinal

    }

    private fun initOnClick() {
        imgFastRewind.setOnClickListener(this)
        imgSkipPrevious.setOnClickListener(this)
        imgPlay.setOnClickListener(this)
        imgSkipNext.setOnClickListener(this)
        imgFastForward.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id ?: return) {
            R.id.imgFastRewind -> {
                Handler().post {
                    mPlayer?.seekTo(sbProgress.progress - 5000)
                }

            }
            R.id.imgSkipPrevious -> {
                if (positionMusic !=0){
                    positionMusic--
                    playMusic(namesMusicList[positionMusic])
                }
            }
            R.id.imgPlay -> {
                if (mPlayer?.isPlaying == true){
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_pause))
                    mPlayer?.pause()
                }else{
                    mPlayer?.start()
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_play))
                }
            }
            R.id.imgSkipNext -> {
                if (positionMusic < namesMusicList.size){
                    positionMusic++
                    playMusic(namesMusicList[positionMusic])
                }
            }
            R.id.imgFastForward -> {
                Handler().post {
                    mPlayer?.seekTo(sbProgress.progress + 5000)
                }

            }

        }
    }

    private fun initProgress() {
        val background = object : Thread() {
            @SuppressLint("SetTextI18n", "DefaultLocale")
            override fun run() {
                val duration = mPlayer?.duration ?: 0
                sbProgress.max = duration
                var positionCurrent = 0
                while (positionCurrent < duration) {
                    try {
                        sleep((500).toLong())
                        positionCurrent = mPlayer?.currentPosition ?: 0
                        sbProgress.progress = positionCurrent
                        val durationTxt: Int = mPlayer?.currentPosition?:0
                        val time = java.lang.String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(durationTxt.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(durationTxt.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
                                durationTxt.toLong()))
                        )

                        txtDuration.text = time
                        Log.d("postioncurrnt",""+time)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        background.start()
    }
}