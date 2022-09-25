package com.myrgb.ledcontroller.domain.util

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import java.time.ZoneOffset

fun RgbAlarm.asEntityDatabaseModel() = RgbAlarmDatabaseEntity(
    id = id,
    timeMinutesOfDay = timeMinutesOfDay,
    dateTimeMillisTheAlarmWasActivatedFor = nextTriggerDateTime.toEpochSecond(ZoneOffset.UTC),
    activated = activated,
    redValue = color.red,
    greenValue = color.green,
    blueValue = color.blue,
    repetitiveAlarmWeekdays = repetitiveAlarmWeekdays
)

fun List<RgbAlarm>.asEntityDatabaseModels() = map { it.asEntityDatabaseModel() }