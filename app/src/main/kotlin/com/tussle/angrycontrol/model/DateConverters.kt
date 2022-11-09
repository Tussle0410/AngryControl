package com.tussle.angrycontrol.model

import androidx.room.TypeConverter
import java.time.*
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value : Long) : LocalDateTime
        = LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId())
    @TypeConverter
    fun dateToString(date : LocalDateTime) : Long
        = date.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()
}