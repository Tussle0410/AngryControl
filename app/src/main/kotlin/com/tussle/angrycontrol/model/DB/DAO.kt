package com.tussle.angrycontrol.model.DB

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DAO {
    @Query("INSERT INTO AngryDate(angryDegree, date) VALUES(:degree, :date)")
    suspend fun insertAngryDate(degree : Int, date : Long)

    @Query("INSERT INTO AngryCount(count_id, count) VALUES(:id, :count)")
    suspend fun insertAngryCount(id : Int, count : Int)

}