package com.meria.playtaylermel.ui.detail.music


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.DATA_NAME_MUSIC
import com.meria.playtaylermel.DATA_POSITION_MUSIC
import com.meria.playtaylermel.R
import com.meria.playtaylermel.Utils.toastGeneric
import com.meria.playtaylermel.extensions.formatTimePlayer
import com.meria.playtaylermel.model.MediaPlayerSingleton
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import kotlinx.android.synthetic.main.activity_detail_music.*
import kotlin.concurrent.thread


class DetailMusicActivity : AppCompatActivity(), View.OnClickListener {

    private var namesMusicList: ArrayList<MusicModel> = ArrayList()
    private var positionMusic: Int = 0
    var handler :Handler = Handler()
    private val cod = 1222

    private var audioManager : AudioManager? = null

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
                MediaPlayerSingleton.getInstanceMusic()?.seekTo(sbProgress.progress)
            }

        })

    }

    override fun onBackPressed() {
        MediaPlayerSingleton.getInstanceMusic()?.stop()
        super.onBackPressed()

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createFloatingWidget() {
        if (!isPermissionGiven()) {
            var intent: Intent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivityForResult(intent, cod)
            }
        } else {
            startFloatingWidgetService()
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    private fun isPermissionGiven(): Boolean {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cod) {
            if (isPermissionGiven()){
                startFloatingWidgetService()
            } else{
                toastGeneric(this,resources.getString(R.string.error_message_music_denied))
            }

        }
    }

    private fun startFloatingWidgetService() {
        val resultIntent = Intent()
        setResult(120, resultIntent)
        finish()
        startService(Intent(this, FloatingWidgetService::class.java))
        finish()
    }

    private fun playMusic(music: String) {
        MediaPlayerSingleton.getInstanceMusic()?.apply {
            this.stop()
        }
        MediaPlayerSingleton.setMediaPlayerSingleton()
        MediaPlayerSingleton.getInstanceMusic()?.setDataSource(music)
        MediaPlayerSingleton.getInstanceMusic()?.prepare()
        MediaPlayerSingleton.getInstanceMusic()?.start()
        initProgress()
        txtDurationFinal.formatTimePlayer(  MediaPlayerSingleton.getInstanceMusic()?.duration?:0)
        MediaPlayerSingleton.getInstanceMusic()?.setOnCompletionListener {
            it.reset()
            musicNext()
        }

    }

    private fun initOnClick() {
        imgFastRewind.setOnClickListener(this)
        imgSkipPrevious.setOnClickListener(this)
        imgPlay.setOnClickListener(this)
        imgSkipNext.setOnClickListener(this)
        imgFastForward.setOnClickListener(this)
        floatingActionButtonService.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id ?: return) {
            R.id.imgFastRewind -> {
                MediaPlayerSingleton.getInstanceMusic()?.seekTo(sbProgress.progress - 5000)
            }
            R.id.imgSkipPrevious -> {
              musicBack()
            }
            R.id.imgPlay -> {
                if (MediaPlayerSingleton.isPlaying){
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_play))
                    MediaPlayerSingleton.getInstanceMusic()?.pause()
                }else{
                    imgPlay.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_pause))
                    MediaPlayerSingleton.getInstanceMusic()?.start()

                }
            }
            R.id.imgSkipNext -> {
                if (positionMusic < namesMusicList.size-1){
                    musicNext()
                }
            }
            R.id.imgFastForward -> {
                MediaPlayerSingleton.getInstanceMusic()?.seekTo(sbProgress.progress + 5000)
            }

            R.id.floatingActionButtonService->{
                createFloatingWidget()
            }

        }
    }

    private fun musicNext(){
        if (positionMusic < namesMusicList.size-1){
            positionMusic++
            playMusic(namesMusicList[positionMusic].path)
        }
    }

    private fun musicBack(){
        if (positionMusic !=0){
            positionMusic--
            playMusic(namesMusicList[positionMusic].path)
        }
    }

    private fun initProgress() {
        thread(start = true){
            val duration =  MediaPlayerSingleton.getInstanceMusic()?.duration ?: 0
            sbProgress.max = duration
            var positionCurrent = 0
            while (positionCurrent < duration) {
                try {
                    Thread.sleep((500).toLong())
                    positionCurrent =   MediaPlayerSingleton.getInstanceMusic()?.currentPosition ?: 0
                    handler.post {
                        sbProgress.progress = positionCurrent
                        txtDuration.formatTimePlayer(  MediaPlayerSingleton.getInstanceMusic()?.currentPosition?:0)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}