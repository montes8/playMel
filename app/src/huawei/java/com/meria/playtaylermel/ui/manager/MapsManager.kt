package com.meria.playtaylermel.ui.manager

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import com.meria.playtaylermel.manager.IMapManager
import com.meria.playtaylermel.util.Utils


class MapsManager : IMapManager, OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var map: HuaweiMap
    private lateinit var marker : Marker

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
            MapsInitializer.setApiKey("484360685040436224")
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
        map?.let { this.map = map

            val builder = LatLngBounds.Builder()
            Thread{
                val list = Utils.listAddress()
                Handler(Looper.getMainLooper()).post {
                    list.forEach {
                        marker = map.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude)
                        ).title(it.name))
                        marker.tag = it

                        builder.include(marker.position)
                        val bounds : LatLngBounds = builder.build()
                        val padding = 200 // offset from edges of the map in pixels
                        val cu : CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding)
                        map.moveCamera(cu)
                    }
                }
            }.start()

        }
    }
}