package com.example.ledcontroller.model

import java.sql.Date
import java.sql.Time

data class Alarm(
    val id: Int,
    val date: Date,
    val time: Time,
    val active: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false

        if (javaClass != other.javaClass)
            return false

        val otherAlarm = other as Alarm
        return id == otherAlarm.id
                && date == otherAlarm.date
                && time == otherAlarm.time
                && active == otherAlarm.active
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + date.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + active.hashCode()
        return result
    }
}