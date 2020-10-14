package com.meria.playtaylermel.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meria.playtaylermel.db.dao.MusicDao
import com.meria.playtaylermel.model.ImageModel
import com.meria.playtaylermel.model.MusicModel

@Database(entities = [ImageModel::class],version = 1)
abstract class DemoDataBase : RoomDatabase(){

    abstract fun musicDao() : MusicDao
}