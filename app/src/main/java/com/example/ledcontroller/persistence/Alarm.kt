package com.example.ledcontroller.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "active") val active: Boolean
)