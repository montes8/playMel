package com.meria.playtaylermel.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meria.playtaylermel.repository.local.database.dao.MusicDao
import com.meria.playtaylermel.repository.local.database.entity.ImageModel

@Database(entities = [ImageModel::class],version = 1)
abstract class DemoDataBase : RoomDatabase(){
    abstract fun musicDao() : MusicDao
}