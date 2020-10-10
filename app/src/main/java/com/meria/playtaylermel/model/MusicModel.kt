package com.meria.playtaylermel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class MusicModel (
    var name : String,
    var path :String
):Parcelable