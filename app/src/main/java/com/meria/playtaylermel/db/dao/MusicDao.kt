package com.meria.playtaylermel.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.meria.playtaylermel.model.ImageModel
import com.meria.playtaylermel.model.MusicModel

@Dao
interface MusicDao {

    @Query("select * from ImageModel")
    fun getListImages(): List<ImageModel>


    @Query("select * from ImageModel where id= :imageId")
    fun getImageId(imageId : Long) : ImageModel

    @Insert
    fun insertImage(imageModel : ImageModel) : Long


    @Delete
    fun deleteImage(imageModel: ImageModel):Int
}