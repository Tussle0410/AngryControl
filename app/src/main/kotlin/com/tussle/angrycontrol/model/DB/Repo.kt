package com.tussle.angrycontrol.model.DB

import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class Repo {
    private val dao = GlobalApplication.DBInstance.dao()

    suspend fun insertAngryDate(degree : Int, date : Long){
        dao.insertAngryDate(degree, date)
    }
    suspend fun insertAngryCount(id : Int, count : Int){
        dao.insertAngryCount(id, count)
    }
}