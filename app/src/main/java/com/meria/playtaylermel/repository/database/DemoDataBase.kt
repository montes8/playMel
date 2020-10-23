package com.meria.playtaylermel.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meria.playtaylermel.repository.database.dao.MusicDao
import com.meria.playtaylermel.repository.database.entity.ImageModel

@Database(entities = [ImageModel::class],version = 1)
abstract class DemoDataBase : RoomDatabase(){
    abstract fun musicDao() : MusicDao
}