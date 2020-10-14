package com.meria.playtaylermel.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.meria.playtaylermel.R

object Utils {
    fun toastGeneric(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    fun messagePrincess(id : Int,context: Context):String{
          return when(id){
            0->  context.resources.getString(R.string.message_welcome)
            1->  context.resources.getString(R.string.message_welcome_two)
            2->  context.resources.getString(R.string.message_welcome_three)
            3->  context.resources.getString(R.string.message_welcome_four)
              else -> context.resources.getString(R.string.message_welcome_four)
          }

    }
}