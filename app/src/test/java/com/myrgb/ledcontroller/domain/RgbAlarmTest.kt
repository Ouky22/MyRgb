package com.myrgb.ledcontroller.domain

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.time.*


class RgbAlarmTest {
    private lateinit var alarm: RgbAlarm

    @Before
    fun initAlarm() {
        alarm = RgbAlarm(0, false, RgbTriplet(0, 0, 0))
    }

    private fun initForNextTriggerDateTimeTest(
        triggerTimeMinutes: Int,
        dateTimeStringForFixedClock: String
    ) {
        alarm = RgbAlarm(triggerTimeMinutes, false, RgbTriplet(0, 0, 0))
        RgbAlarm.clock = Clock.fixed(
            Instant.parse(dateTimeStringForFixedClock),
            ZoneId.of("UTC")
        )
    }

    @Test
    fun `next trigger date-time of one time alarm is today`() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:58:00Z")
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun `next trigger date-time of one time alarm is tomorrow`() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:59:00Z")
        assertEquals(LocalDateTime.of(2024, 1, 1, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun `next trigger date-time of repetitive alarm is today`() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:58:00Z")
        alarm.makeRepetitiveOn(Weekday.SATURDAY)
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun `next trigger date-time of repetitive alarm is tomorrow`() {
        initForNextTriggerDateTimeTest(23 * 60 + 59, "2023-12-31T23:59:00Z")
        alarm.makeRepetitiveOn(Weekday.SATURDAY)
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertEquals(LocalDateTime.of(2024, 1, 1, 23, 59), alarm.nextTriggerDateTime)
    }

    @Test
    fun `next trigger date-time of repetitive alarm is in a week`() {
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
    fun `when alarm is set to be repetitive on monday it should be repetitive on mondays`() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertTrue(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun `when alarm is set to be repetitive on sunday it should be repetitive on sundays`() {
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        assertTrue(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun `when alarm is NOT set to be repetitive on monday it should NOT be repetitive on mondays`() {
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun `when alarm is NOT set to be repetitive on sunday it should NOT be repetitive on sundays`() {
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun `when alarm is no longer repetitive on mondays it should not be repetitive on mondays`() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.MONDAY)
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
    }

    @Test
    fun `when alarm is no longer repetitive on sundays it should not be repetitive on sundays`() {
        alarm.makeRepetitiveOn(Weekday.SUNDAY)
        alarm.makeNotRepetitiveOn(Weekday.SUNDAY)
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun `when alarm is initialized then it is a one time alarm`() {
        assertTrue(alarm.isOneTimeAlarm)
    }

    @Test
    fun `when alarm is initialized then it is not repetitive on any day`() {
        assertFalse(alarm.isRepetitiveOn(Weekday.MONDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.TUESDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.WEDNESDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.THURSDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.FRIDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.SATURDAY))
        assertFalse(alarm.isRepetitiveOn(Weekday.SUNDAY))
    }

    @Test
    fun `when the alarm was repetitive it is now a one time alarm`() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.MONDAY)
        alarm.makeNotRepetitiveOn(Weekday.SUNDAY)
        assertTrue(alarm.isOneTimeAlarm)
    }

    @Test
    fun `when one time alarm was made repetitive it is no longer a one time alarm`() {
        alarm.makeRepetitiveOn(Weekday.MONDAY)
        assertFalse(alarm.isOneTimeAlarm)
    }
}