package com.meria.playtaylermel

import android.content.Context
import android.widget.Toast

object Utils {
    fun toastGeneric(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}