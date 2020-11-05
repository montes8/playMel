package com.meria.playtaylermel.di

import com.meria.playtaylermel.ui.MapsManager
import com.meria.playtaylermel.ui.map.IMapManager
import org.koin.dsl.module

val mapModule = module {
    single<IMapManager> { MapsManager() }
}