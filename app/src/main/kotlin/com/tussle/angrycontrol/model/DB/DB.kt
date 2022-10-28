package com.tussle.angrycontrol.model.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tussle.angrycontrol.model.*

@Database(entities = [AngryDate::class,AngryDiary::class,AngryCount::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class DB : RoomDatabase() {
    abstract fun dao() : DAO
}