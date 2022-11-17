package com.tussle.angrycontrol.model

import androidx.room.Embedded
import java.io.Serializable

data class DateAndDiary(
    @Embedded
    val angryDate: AngryDate,
    @Embedded
    val angryDiary: AngryDiary?
) : Serializable
