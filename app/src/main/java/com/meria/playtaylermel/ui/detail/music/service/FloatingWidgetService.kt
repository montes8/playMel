package com.meria.playtaylermel.ui.detail.music.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import com.meria.playtaylermel.R
import com.meria.playtaylermel.ui.detail.music.DetailMusicActivity
import kotlin.math.abs


class FloatingWidgetService : Service(), View.OnClickListener {

    private var mWindowManager: WindowManager? = null
    private var mFloatingWidgetView: View? = null
    private var collapsedView: View? = null
    private val szWindow = Point()
    private var webView: WebView? = null
    internal var timeStart: Long = 0
    internal var timeEnd: Long = 0
    private var minimise: View? = null


    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            val channelID = "my_channel_01"
            val channel = NotificationChannel(
                channelID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification: Notification = Notification.Builder(this, channelID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
        }

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        getWindowManagerDefaultDisplay()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.let {
            addFloatingWidgetView(inflater)
            implementClickListeners()
            implementTouchListenerToFloatingWidgetView()
            updateExpandedView()
        }

    }


    private fun updateExpandedView() {
        val params = mFloatingWidgetView?.layoutParams as WindowManager.LayoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mWindowManager?.updateViewLayout(mFloatingWidgetView, params)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun addFloatingWidgetView(inflater: LayoutInflater) {
        val frameLayout = object : FrameLayout(this) {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView != null && webView?.canGoBack() == true) {
                        webView?.goBack()
                    } else if (minimise != null) {
                        minimise?.performClick()
                    }
                }

                return super.dispatchKeyEvent(event)
            }
        }
        Log.d("eventFloating", "dispatchKeyEvent")
        mFloatingWidgetView = inflater.inflate(R.layout.floating_service, frameLayout)

        val params = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 0
        mWindowManager?.addView(mFloatingWidgetView, params)
        collapsedView = mFloatingWidgetView?.findViewById(R.id.collapseView)
    }

    private fun getWindowManagerDefaultDisplay() {
        mWindowManager?.defaultDisplay?.getSize(szWindow)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun implementTouchListenerToFloatingWidgetView() {
        val collapsedView =
            mFloatingWidgetView?.findViewById(R.id.collapseView) as ConstraintLayout
        collapsedView.setOnTouchListener(object : View.OnTouchListener {
            private var lastAction: Int = 0
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                Log.d("eventFloating", "onTouch")
                val params = mFloatingWidgetView?.layoutParams as WindowManager.LayoutParams
                val xCord = event.rawX
                val yCord = event.rawY
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        timeStart = System.currentTimeMillis()
                        initialX = params.x
                        initialY = params.y

                        initialTouchX = event.rawX
                        initialTouchY = event.rawY

                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        timeEnd = System.currentTimeMillis()
                        val xDiff = xCord - initialTouchX
                        val yDiff = yCord - initialTouchY
                        if (abs(xDiff) < 5 && abs(yDiff) < 5 && (timeEnd - timeStart < 300)) {
                            clickButtonFloating()
                        }
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        mWindowManager?.updateViewLayout(mFloatingWidgetView, params)
                        lastAction = event.action
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun implementClickListeners() {
        mFloatingWidgetView?.findViewById<ImageView>(R.id.close_floating_view)
            ?.setOnClickListener(this)
        mFloatingWidgetView?.findViewById<ConstraintLayout>(R.id.collapseView)
            ?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.close_floating_view -> {
                stopSelf()
            }

            R.id.collapseView -> {
                clickButtonFloating()
            }
        }
    }

    private fun clickButtonFloating(){
        val intent = Intent(this, DetailMusicActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        stopSelf()

    }


    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingWidgetView != null)
            mWindowManager?.removeView(mFloatingWidgetView)
    }
}