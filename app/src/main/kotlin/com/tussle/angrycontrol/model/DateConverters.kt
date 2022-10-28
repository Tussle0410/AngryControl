package com.tussle.angrycontrol.model

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value : Long?)
        =  value?.let { Date(it) }
    @TypeConverter
    fun dateToTimeStamp(date : Date?) : Long?
        = date?.time
}