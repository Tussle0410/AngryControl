package com.tussle.angrycontrol.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class AngryDate(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val angryDegree : Int,
    val date : Date?
)
