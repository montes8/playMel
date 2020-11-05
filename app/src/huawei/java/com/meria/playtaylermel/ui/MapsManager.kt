package com.meria.playtaylermel.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.meria.playtaylermel.ui.map.IMapManager

class MapsManager : IMapManager, OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var map: HuaweiMap

    override fun configMap(context: Context, savedInstanceState: Bundle?) {
        val options = HuaweiMapOptions().apply {
            compassEnabled(true)
            zoomControlsEnabled(false)
            scrollGesturesEnabled(false)
            zoomGesturesEnabled(false)
        }
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle =
                savedInstanceState.getBundle("MAPVIEW_BUNDLE_KEY")
        }
        mapView = MapView(context, options).apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MapsManager)
        }
    }

    override fun fetchMapView(): View = mapView
    override fun onStart() = mapView.onStart()

    override fun onStop() = mapView.onStop()

    override fun onDestroy() = mapView.onDestroy()

    override fun onPause() = mapView.onPause()

    override fun onResume() = mapView.onResume()

    override fun onLowMemory() = mapView.onLowMemory()

    override fun onMapReady(map: HuaweiMap?) {
        map?.let { this.map = map }
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
    }
}