package com.meria.playtaylermel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class MusicModel (
    val id : Int= 0,
    var name : String,
    var path :String,
    var play : Boolean = false
):Parcelable