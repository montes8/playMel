package com.meria.playtaylermel.repository.network

import retrofit2.Call
import retrofit2.http.GET

interface PlayService {
    @GET("ordenes-proceso")
    fun getData(): Call<ArrayList<Product>>

    companion object {
        fun create(): PlayService {
            return RetrofitCreator
                .getInstanceRetrofit()
                .create(PlayService::class.java)
        }
    }
}