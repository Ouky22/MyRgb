package com.myrgb.ledcontroller.domain

import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

data class RgbAlarm(
    var timeMinutesOfDay: Int,
    var activated: Boolean = false,
    var color: RgbTriplet,
    var lastTimeActivated: LocalDateTime? = null,

    /**
     * The 7 least significant bits of this byte indicate whether the alarm will be triggered on
     * the corresponding day of week.
     * Monday is assigned to the second most significant bit followed by the other week days
     * in regular order (2nd most significant bit -> Monday, ..., least significant bit -> Sunday).
     * The most significant bit has no meaning.
     */
    var repetitiveAlarmWeekdays: Byte = 0b00000000.toByte()
) {
    val triggerTimeHoursOfDay: Int
        get() = timeMinutesOfDay / 60
    val triggerTimeMinutesOfHour: Int
        get() = timeMinutesOfDay % 60

    val triggerTimeString: String
        get() = LocalTime.of(triggerTimeHoursOfDay, triggerTimeMinutesOfHour)
            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

    val nextTriggerDateTimeString: String
        get() = nextTriggerDateTime.format(
            DateTimeFormatter.ofPattern("EEE dd LLL", Locale.getDefault())
        )

    val nextTriggerDateTime: LocalDateTime
        get() = getNextTriggerDateTimeFrom(LocalDateTime.now(clock))

    fun getNextTriggerDateTimeFrom(startDateTime: LocalDateTime): LocalDateTime {
        var nextTriggerDateTime = LocalDateTime.of(
            startDateTime.year,
            startDateTime.month,
            startDateTime.dayOfMonth,
            triggerTimeHoursOfDay,
            triggerTimeMinutesOfHour
        )

        if (isOneTimeAlarm) {
            if (startDateTime.isBefore(nextTriggerDateTime))
                return nextTriggerDateTime
            else
                return nextTriggerDateTime.plusDays(1)
        } else {
            var currentWeekday: Weekday = Weekday.values()
                .find { it.ordinal + 1 == startDateTime.dayOfWeek.value }
                ?: throw Exception("Could not find current weekday")

            while (!isRepetitiveOn(currentWeekday)
                || startDateTime.isAfter(nextTriggerDateTime)
                || startDateTime.isEqual(nextTriggerDateTime)
            ) {
                currentWeekday = currentWeekday.nextWeekday
                nextTriggerDateTime = nextTriggerDateTime.plusDays(1)
            }

            return nextTriggerDateTime
        }
    }

    val isOneTimeAlarm
        get() = repetitiveAlarmWeekdays == 0b00000000.toByte() || repetitiveAlarmWeekdays == 0b10000000.toByte()

    fun isRepetitiveOn(day: Weekday) = (repetitiveAlarmWeekdays and day.bitMask) > 0

    fun makeRepetitiveOn(day: Weekday) {
        repetitiveAlarmWeekdays = repetitiveAlarmWeekdays or day.bitMask
    }

    fun makeNotRepetitiveOn(day: Weekday) {
        repetitiveAlarmWeekdays = repetitiveAlarmWeekdays and day.bitMask.inv()
    }

    companion object {
        // make the clock for all rgbAlarms swappable for testing
        var clock: Clock = Clock.systemDefaultZone()

        val defaultRgbAlarmInstance: RgbAlarm
            get() = RgbAlarm(0, false, RgbTriplet(255, 0, 0))
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








































