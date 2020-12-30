package com.meria.playtaylermel.repository.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageModel (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    var path : String = ""
)