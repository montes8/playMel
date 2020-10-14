package com.meria.playtaylermel.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MusicModel (
    val id : Int= 0,
    var name : String = "",
    var path :String = "",
    var play : Boolean = false
):Parcelable