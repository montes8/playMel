package com.meria.playtaylermel.repository.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCreator {
    private val BASE_URL = "http://192.168.0.4:3000/"

    private var retrofit: Retrofit? = null

    fun getInstanceRetrofit(): Retrofit {
        if(retrofit ==null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}