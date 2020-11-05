package com.meria.playtaylermel.ui.map

import android.content.Context
import android.os.Bundle
import android.view.View

interface IMapManager {
    fun configMap(context: Context, savedInstanceState: Bundle? = null)
    fun fetchMapView(): View

    fun onStart()
    fun onStop()
    fun onDestroy()
    fun onPause()
    fun onResume()
    fun onLowMemory()
}
