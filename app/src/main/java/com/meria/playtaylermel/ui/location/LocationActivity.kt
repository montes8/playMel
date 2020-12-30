package com.meria.playtaylermel.ui.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.meria.playtaylermel.R
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "locationUpdatesCallback"
        private const val TAG_TWO = "locationAvailability"
        private const val TAG_THREE = "locationActivity"
        fun newInstance(context: Context): Intent {
            return Intent(context, LocationActivity::class.java)
        }
    }


    private var mLocationCallback: LocationCallback? = null

    private var mLocationRequest: LocationRequest? = null

    private lateinit var mfusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var settingsClient: SettingsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // check location permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q")
            if (checkSelfPermission(
                    ACCESS_FINE_LOCATION
                ) != PERMISSION_GRANTED
                && checkSelfPermission(
                    ACCESS_COARSE_LOCATION
                ) != PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            if (checkSelfPermission(
                    ACCESS_FINE_LOCATION
                ) != PERMISSION_GRANTED && checkSelfPermission(
                    ACCESS_COARSE_LOCATION
                ) != PERMISSION_GRANTED && checkSelfPermission(
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }


        // create fusedLocationProviderClient
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // create settingsClient
        settingsClient = LocationServices.getSettingsClient(this)

        mLocationRequest = LocationRequest().apply {
            interval = 1000
            needAddress = true
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        requestLocationUpdatesWithCallback()
        getLastLocation()
        getLocationAvailability()
        if (null == mLocationCallback) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null) {
                        val locations: List<Location> =
                            locationResult.locations
                        if (locations.isNotEmpty()) {
                            for (location in locations) {
                                txtLocationOne.text = "${location.longitude} , ${location.latitude}"
                                Log.d(
                                    TAG,
                                    "onLocationResult location[Longitude,Latitude,Accuracy]:${location.longitude} , ${location.latitude} , ${location.accuracy}"
                                )
                            }
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    locationAvailability?.let {
                        val flag: Boolean = locationAvailability.isLocationAvailable
                        Log.d(TAG, "onLocationAvailability isLocationAvailable:$flag")
                    }
                }
            }
        }



    }

    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // check devices settings before request location updates.
            //Before requesting location update, invoke checkLocationSettings to check device settings.
            val locationSettingsResponseTask: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(locationSettingsRequest)

            locationSettingsResponseTask.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
                Log.i(TAG, "check location settings success  {$locationSettingsResponse}")
                // request location updates
                mfusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                    .addOnSuccessListener {
                        Log.i(TAG, "requestLocationUpdatesWithCallback onSuccess")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "requestLocationUpdatesWithCallback onFailure:${e.message}")
                    }
            }
                .addOnFailureListener { e: Exception -> Log.e(TAG, "checkLocationSetting onFailure:${e.message}")
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this, 0
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "requestLocationUpdatesWithCallback exception:${e.message}")
        }
    }

    private fun getLocationAvailability() {
        try {
            val locationAvailability = mfusedLocationProviderClient.locationAvailability
            locationAvailability?.addOnSuccessListener { locationAvail ->
                locationAvailability.let {
                    txtLocationOne.text = "ocationAvail"
                    Log.d(
                        TAG_TWO,
                        "getLocationAvailability onSuccess:$locationAvail"


                    )
                }

            }
                ?.addOnFailureListener { e ->
                    Log.e(TAG_TWO, "getLocationAvailability onFailure:${e.message}")
                }
        } catch (e: Exception) {
            Log.e(TAG_TWO, "getLocationAvailability exception:${e.message}")
        }
    }

    private fun getLastLocation() {
        try {
            val lastLocation =
                mfusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener(OnSuccessListener { location ->
                if (location == null) {
                    Log.d(TAG_THREE, "getLastLocation onSuccess location is null")
                    return@OnSuccessListener
                }

                txtLocationThree.text = "${location.longitude} , ${location.latitude}"

                Log.d(
                    TAG,
                    "getLastLocation onSuccess location[Longitude,Latitude]:${location.longitude},${location.latitude}"
                )
                return@OnSuccessListener
            }).addOnFailureListener { e ->
                Log.e(TAG_THREE, "getLastLocation onFailure:${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG_THREE, "getLastLocation exception:${e.message}")
        }
    }


    override fun onDestroy() {
        // don't need to receive callback
        removeLocationUpdatesWithCallback()
        super.onDestroy()
    }

    private fun removeLocationUpdatesWithCallback() {
        try {
            mfusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                    Log.d(
                        TAG,
                        "removeLocationUpdatesWithCallback onFailure:${e.message}"
                    )
                }
        } catch (e: Exception) {
            Log.d(
                TAG,
                "removeLocationUpdatesWithCallback exception:${e.message}"
            )
        }
    }
}