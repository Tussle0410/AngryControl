package com.tussle.angrycontrol.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = AngryDate::class,
    parentColumns = ["id"],
    childColumns = ["count_id"],
    onDelete = CASCADE
)])
data class AngryCount(
    val count_id : Int,
    val count : Int,
    @PrimaryKey(autoGenerate = true)
    val pk : Int
)
