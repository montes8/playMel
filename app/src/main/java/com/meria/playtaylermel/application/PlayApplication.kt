package com.meria.playtaylermel.application

import android.app.Application
import androidx.room.Room
import com.meria.playtaylermel.di.managerModule
import com.meria.playtaylermel.repository.database.DemoDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class PlayApplication : Application() {
    companion object {

        var database : DemoDataBase? = null
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@PlayApplication)
            modules(
                listOf(
                    managerModule
                )
            )
        }
        database = Room.databaseBuilder(this,
            DemoDataBase::class.java,"play_taymelt.db")
            .build()
    }
}