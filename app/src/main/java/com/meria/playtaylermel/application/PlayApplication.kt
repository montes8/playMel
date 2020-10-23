package com.meria.playtaylermel.application

import android.app.Application
import androidx.room.Room
import com.meria.playtaylermel.repository.database.DemoDataBase

class PlayApplication : Application() {
    companion object {

        var database : DemoDataBase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            DemoDataBase::class.java,"play_taymelt.db")
            .build()
    }
}