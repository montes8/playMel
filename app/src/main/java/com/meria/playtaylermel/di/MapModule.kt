package com.meria.playtaylermel.di

import com.meria.playtaylermel.manager.IMapManager
import com.meria.playtaylermel.ui.manager.MapsManager
import org.koin.dsl.module

val managerModule = module {
    single<IMapManager> { MapsManager() }

}