package com.tussle.angrycontrol.model.DB

import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class Repo {
    private val dao = GlobalApplication.DBInstance.dao()
    //Insert Sql
    suspend fun insertAngryDate(degree : Int, date : Long){
        dao.insertAngryDate(degree, date)
    }
    suspend fun insertAngryCount(id : Int, count : Int){
        dao.insertAngryCount(id, count)
    }
    suspend fun insertAngryDiary(id : Int, content : String){
        dao.insertAngryDiary(id, content)
    }
    //Select Sql
    suspend fun selectAngryDate()
        = dao.selectAngryDate()

    suspend fun selectAngryDateAndDiary()
        = dao.selectAngryDateAndDiary()
}