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
import com.meria.playtaylermel.R
import com.meria.playtaylermel.util.Utils.toastGeneric
import com.meria.playtaylermel.extensions.formatTimePlayer
import com.meria.playtaylermel.model.MediaPlayerSingleton
import com.meria.playtaylermel.model.MusicModel
import com.meria.playtaylermel.model.MusicTemporal
import com.meria.playtaylermel.ui.detail.music.service.FloatingWidgetService
import com.meria.playtaylermel.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_detail_music.*
import kotlin.concurrent.thread


class DetailMusicActivity : AppCompatActivity(), View.OnClickListener {

    private var namesMusicList: ArrayList<MusicModel> = ArrayList()
    private var positionMusic: Int = 0
    var handler :Handler = Handler()
    private val cod = 1222

    private var audioManager : AudioManager? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return  Intent(context, DetailMusicActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_music)
        namesMusicList = MusicTemporal.getListMusic()
        positionMusic = MusicTemporal.getPositionMusic()
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
        startActivity(MainActivity.newInstance(this))

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
        MediaPlayerSingleton.getInstanceMusic()
        val resultIntent = Intent()
        setResult(120, resultIntent)
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
            R.id.imgFastRewind -> { MediaPlayerSingleton.getInstanceMusic()?.seekTo(sbProgress.progress - 5000)}
            R.id.imgSkipPrevious -> { musicBack() }
            R.id.imgPlay -> {
                if (MediaPlayerSingleton.getInstanceMusic()?.isPlaying==true){
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
            R.id.imgFastForward -> { MediaPlayerSingleton.getInstanceMusic()?.seekTo(sbProgress.progress + 5000) }

            R.id.floatingActionButtonService->{ createFloatingWidget() }

        }
    }

    private fun musicNext(){
        if (positionMusic < namesMusicList.size-1){
            positionMusic++
            MusicTemporal.setPositionCurrentMusic(0)
            MusicTemporal.setPositionMusic(positionMusic)
            playMusic(namesMusicList[positionMusic].path)
        }
    }

    private fun musicBack(){
        if (positionMusic !=0){
            positionMusic--
            MusicTemporal.setPositionCurrentMusic(0)
            MusicTemporal.setPositionMusic(positionMusic)
            playMusic(namesMusicList[positionMusic].path)
        }
    }

    private fun initProgress() {
        thread(start = true){
            val duration =  MediaPlayerSingleton.getInstanceMusic()?.duration ?: 0
            sbProgress.max = duration
            MediaPlayerSingleton.getInstanceMusic()?.seekTo(MusicTemporal.getPositionCurrentMusic())
            var positionCurrent = 0
            while (positionCurrent < duration) {
                try {
                    Thread.sleep((500).toLong())
                    MusicTemporal.setPositionCurrentMusic(MediaPlayerSingleton.getInstanceMusic()?.currentPosition ?: 0)
                    positionCurrent =   MusicTemporal.getPositionCurrentMusic()
                    handler.post {
                        sbProgress.progress =  MusicTemporal.getPositionCurrentMusic()
                        txtDuration.formatTimePlayer(   MusicTemporal.getPositionCurrentMusic())
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}