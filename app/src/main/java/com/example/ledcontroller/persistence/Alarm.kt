package com.example.ledcontroller.persistence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "triggerTime") var triggerTime: Long,
    @ColumnInfo(name = "isActive") var activated: Boolean = false,

    /**
     * The 7 least significant bits of this byte indicate whether the alarm will be triggered on
     * the corresponding day of week.
     * Monday is assigned to the second most significant bit followed by the other week days
     * in regular order (2nd most significant bit -> Monday, ..., least significant bit -> Sunday).
     * The most significant bit has no meaning.
     */
    @ColumnInfo(name = "alarmWeekDays")
    private var alarmWeekDays: Byte = 0b00000000.toByte()
) {
    val hours: Int
        get() = (triggerTime / 60).toInt()
    val minutes: Int
        get() = (triggerTime % 60).toInt()

    val triggerTimeString: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return LocalTime.of(hours, minutes)
                .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        }

    val nextTriggerDate: LocalDate
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return LocalDate.now()
        }

    val nextTriggerDateString: String
        get() {

            return ""
        }

    val isOneTimeAlarm
        get() = alarmWeekDays == 0b00000000.toByte() || alarmWeekDays == 0b10000000.toByte()


    fun isRepetitiveOn(day: Weekday) = (alarmWeekDays and day.bitMask) > 0

    fun makeRepetitiveOn(day: Weekday) {
        alarmWeekDays = alarmWeekDays or day.bitMask
    }

    fun makeNotRepetitiveOn(day: Weekday) {
        alarmWeekDays = alarmWeekDays and day.bitMask.inv()
    }
}

enum class Weekday(val bitMask: Byte) {
    MONDAY(0b01000000.toByte()),
    TUESDAY(0b00100000.toByte()),
    WEDNESDAY(0b00010000.toByte()),
    THURSDAY(0b00001000.toByte()),
    FRIDAY(0b00000100.toByte()),
    SATURDAY(0b00000010.toByte()),
    SUNDAY(0b00000001.toByte())
}