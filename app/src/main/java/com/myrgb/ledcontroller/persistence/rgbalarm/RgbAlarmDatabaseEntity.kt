package com.myrgb.ledcontroller.persistence.rgbalarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rgbAlarm")
data class RgbAlarmDatabaseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "trigger_time_minutes_of_day") val triggerTimeMinutesOfDay: Int,
    @ColumnInfo(name = "is_active") val activated: Boolean,
    @ColumnInfo(name = "red_value") val redValue: Int,
    @ColumnInfo(name = "green_value") val greenValue: Int,
    @ColumnInfo(name = "blue_value") val blueValue: Int,
    @ColumnInfo(name = "repetitive_alarm_weekdays") val repetitiveAlarmWeekdays: Byte
)