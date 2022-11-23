package com.tussle.angrycontrol.model.DB

import androidx.room.Dao
import androidx.room.Query
import com.tussle.angrycontrol.model.AngryDate
import com.tussle.angrycontrol.model.AngryDiary
import com.tussle.angrycontrol.model.DateAndDiary

@Dao
interface DAO {
    //Insert Sql
    @Query("INSERT INTO AngryDate(id, angryDegree, date) VALUES(:id,:degree, :date)")
    suspend fun insertAngryDate(id:Int, degree : Int, date : Long)

    @Query("INSERT INTO AngryCount(count_id, count) VALUES(:id, :count)")
    suspend fun insertAngryCount(id : Int, count : Int)

    @Query("INSERT INTO AngryDiary(diary_id, content) VALUES(:id, :content)")
    suspend fun insertAngryDiary(id : Int, content : String)

    //Select Sql

    @Query("SELECT * FROM AngryDate LEFT OUTER JOIN AngryDiary ON AngryDiary.diary_id == AngryDate.id ORDER BY AngryDate.date DESC")
    suspend fun selectAngryDateAndDiary() : MutableList<DateAndDiary>

    @Query("SELECT id FROM AngryDate ORDER BY id DESC LIMIT 1")
    suspend fun selectAngryCount() : Int?
    //Delete Sql
    @Query("DELETE FROM AngryDate WHERE id == :id")
    suspend fun deleteAngryDiary(id : Int?)

    //Update Sql
    @Query("UPDATE AngryDiary SET content = :content WHERE diary_id == :id")
    suspend fun updateAngryDiary(content : String, id : Int)

    @Query("UPDATE AngryDate SET angryDegree = :degree WHERE id == :id")
    suspend fun updateAngryDate(degree: Int, id : Int)

}