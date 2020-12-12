package com.meria.playtaylermel.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.maps.*

import com.huawei.hms.maps.model.*
import com.meria.playtaylermel.R
import com.meria.playtaylermel.manager.IMapManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

class MapsActivity : AppCompatActivity(), KoinComponent {

    private val mapManager: IMapManager by inject()

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }

        private const val REQUEST_CODE = 100
        private val RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }
        configMap(savedInstanceState)

       // MapsInitializer.setApiKey("CgB6e3x9/lg/dpZxOHpObNiIAd95BDBLKye5zVt+iHkCjimKGSBnV0kAEAGVIsKbjV6X3q0yBTecYAb3ls2ygeac")

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

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

}