package com.meria.playtaylermel.ui.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

class MapsActivity : AppCompatActivity(), KoinComponent {
    private val mapManager: IMapManager by inject()


    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configMap(savedInstanceState)
    }

    private fun configMap(savedInstanceState: Bundle?) {
        mapManager.configMap(this, savedInstanceState)
        val map = mapManager.fetchMapView()
        setContentView(map)
    }

    override fun onStart() {
        super.onStart()
        mapManager.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapManager.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapManager.onDestroy()
    }

    override fun onPause() {
        mapManager.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapManager.onResume()
    }

}