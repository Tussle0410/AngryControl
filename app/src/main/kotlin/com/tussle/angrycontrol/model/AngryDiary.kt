package com.tussle.angrycontrol.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = AngryDate::class,
    parentColumns = ["id"],
    childColumns = ["diary_id"],
    onDelete = ForeignKey.CASCADE,
)]
)
data class AngryDiary(
    val diary_id : Int,
    val content : String,
    @PrimaryKey(autoGenerate = true)
    val pk : Int
)
