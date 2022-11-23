package com.tussle.angrycontrol.model.DB

import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class Repo {
    private val dao = GlobalApplication.DBInstance.dao()
    //Insert Sql
    suspend fun insertAngryDate(id:Int,degree : Int, date : Long){
        dao.insertAngryDate(id, degree, date)
    }
    suspend fun insertAngryCount(id : Int, count : Int){
        dao.insertAngryCount(id, count)
    }
    suspend fun insertAngryDiary(id : Int, content : String){
        dao.insertAngryDiary(id, content)
    }
    //Select Sql
    suspend fun selectAngryDateAndDiary()
        = dao.selectAngryDateAndDiary()
    suspend fun selectAngryCount()
        = dao.selectAngryCount()
    //Delete Sql
    suspend fun deleteAngryDiary(id : Int?){
        dao.deleteAngryDiary(id)
    }
    //Update Sql
    suspend fun updateAngryDiary(content : String, id : Int){
        dao.updateAngryDiary(content, id)
    }
    suspend fun updateAngryDate(degree: Int, id: Int){
        dao.updateAngryDate(degree, id)
    }
}