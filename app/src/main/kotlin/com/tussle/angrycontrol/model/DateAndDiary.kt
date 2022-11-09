package com.tussle.angrycontrol.model

import androidx.room.Embedded

data class DateAndDiary(
    @Embedded
    val angryDate: AngryDate,
    @Embedded
    val angryDiary: AngryDiary
)
