package com.meria.playtaylermel.model

object MusicTemporal {

    private var dataList : ArrayList<MusicModel> = ArrayList()
    private var positionMusic = 0

    fun getListMusic()= dataList


    fun addListMusic(list : ArrayList<MusicModel>){
        dataList = list
    }

    fun setPositionMusic(position : Int){
        this.positionMusic = position
    }

    fun getPositionMusic()= positionMusic



    fun resetListMusic(){
         dataList.clear()
        positionMusic = 0
    }
}