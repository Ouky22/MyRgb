package com.myrgb.ledcontroller.persistence.rgbalarm

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "rgbAlarm", primaryKeys = ["time_minutes_of_day"])
data class RgbAlarmDatabaseEntity(
    @ColumnInfo(name = "time_minutes_of_day") val timeMinutesOfDay: Int,
    @ColumnInfo(name = "last_time_activated_seconds") val lastTimeActivatedSeconds: Long?,
    @ColumnInfo(name = "is_active") val activated: Boolean,
    @ColumnInfo(name = "red_value") val redValue: Int,
    @ColumnInfo(name = "green_value") val greenValue: Int,
    @ColumnInfo(name = "blue_value") val blueValue: Int,
    @ColumnInfo(name = "repetitive_alarm_weekdays") val repetitiveAlarmWeekdays: Byte
)