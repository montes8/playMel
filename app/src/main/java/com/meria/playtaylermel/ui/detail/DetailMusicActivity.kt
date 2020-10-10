package com.meria.playtaylermel.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.DATA_NAME_MUSIC
import com.meria.playtaylermel.DATA_POSITION_MUSIC
import com.meria.playtaylermel.R
import com.meria.playtaylermel.extensions.formatTimePlayer
import com.meria.playtaylermel.model.MusicModel
import kotlinx.android.synthetic.main.activity_detail_music.*


class DetailMusicActivity : AppCompatActivity(), View.OnClickListener {

    private var namesMusicList: ArrayList<MusicModel> = ArrayList()
    private var positionMusic: Int = 0


    private var audioManager : AudioManager? = null
    private var mPlayer: MediaPlayer? = null

    companion object {
        fun newInstance(context: Context, nameMusic: ArrayList<MusicModel>, position: Int): Intent {
            val intent = Intent(context, DetailMusicActivity::class.java)
            intent.putExtra(DATA_NAME_MUSIC, nameMusic)
            intent.putExtra(DATA_POSITION_MUSIC, position)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_music)
        intent?.let {
            namesMusicList = it.getParcelableArrayListExtra<MusicModel>(DATA_NAME_MUSIC) as ArrayList<MusicModel>
            positionMusic = it.getIntExtra(DATA_POSITION_MUSIC, 0)
        }
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        seekBarAudio.max = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)?:0
        seekBarAudio.progress= audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)?:0

       initUpdateProgress()
        initOnClick()
        playMusic(namesMusicList[positionMusic].path)


    }

    private fun initUpdateProgress(){
        seekBarAudio.setOnSeekBarChangeListener(object  : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        sbProgress.setOnSeekBarChangeListener(object  : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mPlayer?.seekTo(sbProgress.progress)
            }

        })

    }

    override fun onBackPressed() {
        mPlayer?.stop()
        super.onBackPressed()

    }

    @SuppressLint("DefaultLocale")
    private fun playMusic(music: String) {
        mPlayer?.apply {
            this.stop()
        }
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(music)
        mPlayer?.prepare()
        mPlayer?.start()
        initProgress()
        txtDurationFinal.formatTimePlayer(mPlayer?.duration?:0)

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
                    mPlayer?.seekTo(sbProgress.progress - 5000)
            }
            R.id.imgSkipPrevious -> {
                if (positionMusic !=0){
                    positionMusic--
                    playMusic(namesMusicList[positionMusic].path)
                }
            }
            R.id.imgPlay -> {
                if (mPlayer?.isPlaying == true){
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_play))
                    mPlayer?.pause()
                }else{
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_pause))
                    mPlayer?.start()

                }
            }
            R.id.imgSkipNext -> {
                if (positionMusic < namesMusicList.size){
                    positionMusic++
                    playMusic(namesMusicList[positionMusic].path)
                }
            }
            R.id.imgFastForward -> {
                mPlayer?.seekTo(sbProgress.progress + 5000)
            }

        }
    }

    private fun initProgress() {
        val background = object : Thread() {
            override fun run() {
                val duration = mPlayer?.duration ?: 0
                sbProgress.max = duration
                var positionCurrent = 0
                while (positionCurrent < duration) {
                    try {
                        sleep((500).toLong())
                        positionCurrent = mPlayer?.currentPosition ?: 0
                        sbProgress.progress = positionCurrent
                        txtDuration.formatTimePlayer(mPlayer?.currentPosition?:0)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        background.start()
    }
}