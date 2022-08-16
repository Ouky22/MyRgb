package com.example.ledcontroller.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Exception
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "triggerTimeMinutesOfDay") var triggerTimeMinutesOfDay: Int,
    @ColumnInfo(name = "isActive") var activated: Boolean = false,

    /**
     * The 7 least significant bits of this byte indicate whether the alarm will be triggered on
     * the corresponding day of week.
     * Monday is assigned to the second most significant bit followed by the other week days
     * in regular order (2nd most significant bit -> Monday, ..., least significant bit -> Sunday).
     * The most significant bit has no meaning.
     */
    @ColumnInfo(name = "repetitiveAlarmWeekDays")
    private var repetitiveAlarmWeekDays: Byte = 0b00000000.toByte()
) {
    private var clock = Clock.systemDefaultZone()

    val triggerTimeHoursOfDay: Int
        get() = triggerTimeMinutesOfDay / 60
    val triggerTimeMinutesOfHour: Int
        get() = triggerTimeMinutesOfDay % 60

    val triggerTimeString: String
        get() = LocalTime.of(triggerTimeHoursOfDay, triggerTimeMinutesOfHour)
            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

    val nextTriggerDateTimeString: String
        get() = nextTriggerDateTime.format(
            DateTimeFormatter.ofPattern("EEE dd LLL", Locale.getDefault())
        )

    val nextTriggerDateTime: LocalDateTime
        get() {
            val currentDateTime = LocalDateTime.now(clock)
            var nextTriggerDateTime = LocalDateTime.of(
                currentDateTime.year,
                currentDateTime.month,
                currentDateTime.dayOfMonth,
                triggerTimeHoursOfDay,
                triggerTimeMinutesOfHour
            )

            if (isOneTimeAlarm) {
                if (currentDateTime.isBefore(nextTriggerDateTime))
                    return nextTriggerDateTime
                else
                    return nextTriggerDateTime.plusDays(1)
            } else {
                var currentWeekday: Weekday = Weekday.values()
                    .find { it.ordinal + 1 == currentDateTime.dayOfWeek.value }
                    ?: throw Exception("Could not find current weekday")

                while (!isRepetitiveOn(currentWeekday)
                    || currentDateTime.isAfter(nextTriggerDateTime)
                    || currentDateTime.isEqual(nextTriggerDateTime)
                ) {
                    currentWeekday = currentWeekday.nextWeekday
                    nextTriggerDateTime = nextTriggerDateTime.plusDays(1)
                }

                return nextTriggerDateTime
            }
        }

    val isOneTimeAlarm
        get() = repetitiveAlarmWeekDays == 0b00000000.toByte() || repetitiveAlarmWeekDays == 0b10000000.toByte()


    fun isRepetitiveOn(day: Weekday) = (repetitiveAlarmWeekDays and day.bitMask) > 0

    fun makeRepetitiveOn(day: Weekday) {
        repetitiveAlarmWeekDays = repetitiveAlarmWeekDays or day.bitMask
    }

    fun makeNotRepetitiveOn(day: Weekday) {
        repetitiveAlarmWeekDays = repetitiveAlarmWeekDays and day.bitMask.inv()
    }

    fun setClockForTesting(clock: Clock) {
        this.clock = clock
    }
}

/**
 * This enum class represents weekdays in regular order (starting with monday).
 * The order of the enum constants is important, so that the ordinals can be used for computations.
 * The bit mask of a day indicates the position of a weekday in a byte.
 */
enum class Weekday(val bitMask: Byte) {
    MONDAY(0b01000000.toByte()),
    TUESDAY(0b00100000.toByte()),
    WEDNESDAY(0b00010000.toByte()),
    THURSDAY(0b00001000.toByte()),
    FRIDAY(0b00000100.toByte()),
    SATURDAY(0b00000010.toByte()),
    SUNDAY(0b00000001.toByte());

    val nextWeekday: Weekday
        get() = values()[(this.ordinal + 1) % 7]
}









































