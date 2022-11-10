package com.tussle.angrycontrol.model.DB

import androidx.room.Dao
import androidx.room.Query
import com.tussle.angrycontrol.model.AngryDate
import com.tussle.angrycontrol.model.AngryDiary
import com.tussle.angrycontrol.model.DateAndDiary

@Dao
interface DAO {
    //Insert Sql
    @Query("INSERT INTO AngryDate(angryDegree, date) VALUES(:degree, :date)")
    suspend fun insertAngryDate(degree : Int, date : Long)

    @Query("INSERT INTO AngryCount(count_id, count) VALUES(:id, :count)")
    suspend fun insertAngryCount(id : Int, count : Int)

    @Query("INSERT INTO AngryDiary(diary_id, content) VALUES(:id, :content)")
    suspend fun insertAngryDiary(id : Int, content : String)

    //Select Sql
    @Query("SELECT * FROM AngryDate")
    suspend fun selectAngryDate() : MutableList<AngryDate>

    @Query("SELECT * FROM AngryDiary as Diary, AngryDate as Date WHERE Diary.diary_id == Date.id ORDER BY Date.date DESC")
    suspend fun selectAngryDateAndDiary() : MutableList<DateAndDiary>

    //Delete Sql
    @Query("DELETE FROM AngryDiary WHERE diary_id == :id")
    suspend fun deleteAngryDiary(id : Int)

}