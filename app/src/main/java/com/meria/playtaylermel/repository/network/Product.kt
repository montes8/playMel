package com.meria.playtaylermel.repository.network

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("nombre")
    var nombre : String?,
    @SerializedName("cantidad")
    var cantidad : Int?,
    @SerializedName("estado")
    var estado : String?,
    @SerializedName("mesa")
    var mesa : Int?
)