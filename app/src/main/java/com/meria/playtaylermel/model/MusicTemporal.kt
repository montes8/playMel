package com.meria.playtaylermel.model

object MusicTemporal {

    private var dataList : ArrayList<MusicModel> = ArrayList()
    private var positionMusic = 0
    private var positionCurrent = 0

    fun getListMusic()= dataList


    fun addListMusic(list : ArrayList<MusicModel>){
        dataList = list
    }

    fun setPositionMusic(position : Int){
        this.positionMusic = position
    }

    fun getPositionMusic()= positionMusic

    fun setPositionCurrentMusic(currentPosition : Int){
        positionCurrent = currentPosition
    }

    fun getPositionCurrentMusic()= positionCurrent



    fun resetListMusic(){
         dataList.clear()
        positionMusic = 0
    }
}