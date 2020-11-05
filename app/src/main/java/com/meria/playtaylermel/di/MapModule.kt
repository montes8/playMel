package com.meria.playtaylermel.di

import com.meria.playtaylermel.ui.UpdateVersionManager
import com.meria.playtaylermel.ui.map.MapsManager
import com.meria.playtaylermel.manager.IMapManager
import com.meria.playtaylermel.manager.IUpdateVersionManager
import org.koin.dsl.module

val managerModule = module {
    single<IMapManager> { MapsManager() }
    single <IUpdateVersionManager>{ UpdateVersionManager() }
}