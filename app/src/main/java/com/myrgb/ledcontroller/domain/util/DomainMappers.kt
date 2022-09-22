package com.myrgb.ledcontroller.domain.util

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity

fun RgbAlarm.asEntityDatabaseModel() = RgbAlarmDatabaseEntity(
    id = id,
    triggerTimeMinutesOfDay = triggerTimeMinutesOfDay,
    activated = activated,
    redValue = color.red,
    greenValue = color.green,
    blueValue = color.blue,
    repetitiveAlarmWeekdays = repetitiveAlarmWeekdays
)

fun List<RgbAlarm>.asEntityDatabaseModels() = map { it.asEntityDatabaseModel() }