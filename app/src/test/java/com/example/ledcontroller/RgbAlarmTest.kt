package com.example.ledcontroller

import com.example.ledcontroller.model.RgbAlarm
import com.example.ledcontroller.model.Weekday
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.time.*


class RgbAlarmTest {
    private lateinit var alarm: RgbAlarm

    @Before
    fun initAlarm() {
        alarm = RgbAlarm(0, 0, false, 0, 0, 0)
    }

    private fun initForNextTriggerDateTimeTest(
        triggerTimeMinutes: Int,
        dateTimeStringForFixedClock: String
    ) {
        alarm = RgbAlarm(0, triggerTimeMinutes, false, 0, 0, 0)
        alarm.setClockForTesting(
            Clock.fixed(
                Instant.parse(dateTimeStringForFixedClock),
                ZoneId.of("UTC")
            )
        )
    }

    @Test
    fun nextTriggerDateTimeOfOneTimeAlarmToday() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:58:00Z")
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun nextTriggerDateTimeOfOneTimeAlarmTomorrow() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:59:00Z")
        assertEquals(LocalDateTime.of(2024, 1, 1, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun nextTriggerDateTimeOfRepetitiveAlarmToday() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:58:00Z")
        alarm.makeRepetitiveOn(Weekday.SATURDAY)
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun nextTriggerDateTimeOfRepetitiveAlarmTomorrow() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:59:00Z")
        alarm.makeRepetitiveOn(Weekday.SATURDAY)
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertEquals(LocalDateTime.of(2024, 1, 1, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun nextTriggerDateTimeOfRepetitiveAlarmInAWeek() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:59:00Z")
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        assertEquals(LocalDateTime.of(2024, 1, 7, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun getNextWeekdayAfterSunday() {
        assertEquals(Weekday.MONDAY, Weekday.SUNDAY.nextWeekday)
    }

    @Test
    fun getNextWeekdayAfterMonday() {
        assertEquals(Weekday.TUESDAY, Weekday.MONDAY.nextWeekday)
    }

    @Test
    fun checkAlarmRepeatsOnMondays() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertTrue(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun checkAlarmRepeatsOnSundays() {
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        assertTrue(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun checkAlarmNotRepeatsOnMondays() {
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun checkAlarmNotRepeatsOnSundays() {
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun makeNotRepetitiveOnMondays() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.MONDAY)
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun makeNotRepetitiveOnSundays() {
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeNotRepetitiveOn(Weekday.SUNDAY)
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun oneTimeAlarmAfterInitialization() {
        assertTrue(alarm.isOneTimeAlarm)
    }

    @Test
    fun notRepetitiveAfterInitialization() {
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.TUESDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.WEDNESDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.THURSDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.FRIDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.SATURDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun oneTimeAlarmAfterMakingAllDaysNonRepetitive() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.SUNDAY)
        assertTrue(alarm.isOneTimeAlarm)
    }

    @Test
    fun notOneTimeAlarmAfterMakingDayRepetitive() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertFalse(alarm.isOneTimeAlarm)
    }
}