package com.myrgb.ledcontroller.persistence.util

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

fun RgbAlarmDatabaseEntity.asDomainModel() = RgbAlarm(
    timeMinutesOfDay = timeMinutesOfDay,
    lastTimeActivated = lastTimeActivatedSeconds?.let {
        LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
    },
    activated = activated,
    color = RgbTriplet(redValue, greenValue, blueValue),
    repetitiveAlarmWeekdays = repetitiveAlarmWeekdays
)

fun List<RgbAlarmDatabaseEntity>.asDomainModels() = map { it.asDomainModel() }