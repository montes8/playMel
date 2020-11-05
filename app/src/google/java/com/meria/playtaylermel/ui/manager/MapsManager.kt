package com.meria.playtaylermel.ui.manager

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.meria.playtaylermel.manager.IMapManager
import com.meria.playtaylermel.util.Utils.listAddress


class MapsManager : IMapManager, OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var marker : Marker
    override fun configMap(context: Context, savedInstanceState: Bundle?) {
        val options = GoogleMapOptions().apply {
            compassEnabled(true)
            zoomControlsEnabled(true)
            scrollGesturesEnabled(true)
            zoomGesturesEnabled(true)
        }
        mapView = MapView(context, options).apply {
            onCreate(savedInstanceState)
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

    override fun onMapReady(map: GoogleMap?) {
        map?.let { this.map = map
            val builder = LatLngBounds.Builder()
            Thread{
                val list = listAddress()
                Handler(Looper.getMainLooper()).post {
                    list.forEach {
                        marker = map.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title(it.name))
                        marker.tag = it

                        builder.include(marker.position)
                        val bounds : LatLngBounds= builder.build()
                        val padding = 200 // offset from edges of the map in pixels
                        val cu :CameraUpdate= CameraUpdateFactory.newLatLngBounds(bounds,padding)
                        map.moveCamera(cu)
                    }
                }
            }.start()
        }

    }
}