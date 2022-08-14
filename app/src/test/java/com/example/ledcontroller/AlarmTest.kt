package com.example.ledcontroller

import com.example.ledcontroller.persistence.Alarm
import com.example.ledcontroller.persistence.Weekday
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class AlarmTest {
    lateinit var alarm: Alarm

    @Before
    fun initAlarm() {
        alarm = Alarm(0, 0, false)
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