package com.myrgb.ledcontroller.persistence.rgbalarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rgbAlarm")
data class RgbAlarmDatabaseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "time_minutes_of_day") val timeMinutesOfDay: Int,
    @ColumnInfo(name = "date_time_millis_the_alarm_was_activated_for") val dateTimeMillisTheAlarmWasActivatedFor: Long,
    @ColumnInfo(name = "is_active") val activated: Boolean,
    @ColumnInfo(name = "red_value") val redValue: Int,
    @ColumnInfo(name = "green_value") val greenValue: Int,
    @ColumnInfo(name = "blue_value") val blueValue: Int,
    @ColumnInfo(name = "repetitive_alarm_weekdays") val repetitiveAlarmWeekdays: Byte
)