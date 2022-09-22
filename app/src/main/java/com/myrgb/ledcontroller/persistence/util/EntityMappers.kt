package com.myrgb.ledcontroller.persistence.util

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity

fun RgbAlarmDatabaseEntity.asDomainModel() = RgbAlarm(
    id = id,
    triggerTimeMinutesOfDay = triggerTimeMinutesOfDay,
    activated = activated,
    color = RgbTriplet(redValue, greenValue, blueValue),
    repetitiveAlarmWeekdays = repetitiveAlarmWeekdays
)

fun List<RgbAlarmDatabaseEntity>.asDomainModels() = map { it.asDomainModel() }