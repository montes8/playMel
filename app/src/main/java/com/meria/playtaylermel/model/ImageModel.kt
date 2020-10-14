package com.meria.playtaylermel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageModel (
    @PrimaryKey(autoGenerate = true)
    val id : Long= 0,
    var path : String = ""
)