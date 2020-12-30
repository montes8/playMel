package com.meria.playtaylermel.repository.local

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(val context: Context) {

    private val SHARE_PREFERENCES_NAME = "playmeria"

    private val BADGET = SHARE_PREFERENCES_NAME + "badget"


    private val sharePreferences: SharedPreferences

    init {
        sharePreferences = context.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun clear(){
        sharePreferences.edit().clear().apply()
    }


    fun setBadget(badget: Int){
        savePreference(BADGET, badget)
    }

    fun getBadget(): Int? {
        return sharePreferences.getInt(BADGET, 0)
    }



    private fun savePreference(strPrefKey: String, objPrefValue: Any) {
        val editor = sharePreferences.edit()
        when (objPrefValue) {
            is String -> editor.putString(strPrefKey, objPrefValue)
            is Boolean -> editor.putBoolean(strPrefKey, objPrefValue)
            is Int -> editor.putInt(strPrefKey, objPrefValue)
            is Long -> editor.putLong(strPrefKey, objPrefValue)
            is Float -> editor.putFloat(strPrefKey, objPrefValue)
        }
        val success = editor.commit()
    }
}