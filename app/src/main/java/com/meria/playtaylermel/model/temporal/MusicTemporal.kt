package com.meria.playtaylermel.model.temporal

import com.meria.playtaylermel.model.MusicModel

object MusicTemporal {

    private var dataList : ArrayList<MusicModel> = ArrayList()
    private var positionMusic = 0
    private var positionCurrent = 0

    fun getListMusic()=
        dataList


    fun addListMusic(list : ArrayList<MusicModel>){
        dataList = list
    }

    fun setPositionMusic(position : Int){
        positionMusic = position
    }

    fun getPositionMusic()=
        positionMusic

    fun setPositionCurrentMusic(currentPosition : Int){
        positionCurrent = currentPosition
    }

    fun getPositionCurrentMusic()=
        positionCurrent



    fun resetListMusic(){
         dataList.clear()
        positionMusic = 0
    }
}