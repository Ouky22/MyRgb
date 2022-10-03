package com.myrgb.ledcontroller.persistence.rgbalarm

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@ExperimentalCoroutinesApi
class RgbAlarmRepositoryTest {
    private lateinit var fakeRgbAlarmDao: FakeRgbAlarmDao
    private lateinit var repository: RgbAlarmRepository

    @Before
    fun setup() {
        fakeRgbAlarmDao = FakeRgbAlarmDao()
        repository = RgbAlarmRepository(fakeRgbAlarmDao)
    }

    @Test
    fun `when repository is initialized then all expired one time alarms are disabled`() = runTest {
        val emittedValues = mutableListOf<List<RgbAlarm>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            repository.alarms.toList(emittedValues)
        }

        // set clock to 9:00 am
        RgbAlarm.clock = Clock.fixed(
            Instant.parse("2023-12-31T09:00:00Z"),
            ZoneId.of("UTC")
        )
        // activate alarms between 10:00 and 10:30 am
        val activeOneTimeAlarm = RgbAlarm(10 * 60, true, RgbTriplet(0, 0, 0))
        val disabledOneTimeAlarm = RgbAlarm( 10 * 60 + 15, false, RgbTriplet(0, 0, 0))
        val activeRepetitiveAlarm = RgbAlarm( 10 * 60 + 30, true, RgbTriplet(0, 0, 0)).apply {
            makeRepetitiveOn(Weekday.MONDAY)
        }
        fakeRgbAlarmDao.insertOrReplace(activeOneTimeAlarm.asEntityDatabaseModel())
        fakeRgbAlarmDao.insertOrReplace(disabledOneTimeAlarm.asEntityDatabaseModel())
        fakeRgbAlarmDao.insertOrReplace(activeRepetitiveAlarm.asEntityDatabaseModel())
        // set clock to 11.00
        RgbAlarm.clock = Clock.fixed(
            Instant.parse("2023-12-31T11:00:00Z"),
            ZoneId.of("UTC")
        )
        // repository should receive the alarms..
        fakeRgbAlarmDao.emitAlarms()
        // ..and disables expired one-time alarms
        assertFalse(emittedValues.last().first { it.timeMinutesOfDay == activeOneTimeAlarm.timeMinutesOfDay }.activated)
        assertFalse(emittedValues.last().first { it.timeMinutesOfDay == disabledOneTimeAlarm.timeMinutesOfDay }.activated)
        assertTrue(emittedValues.last().first { it.timeMinutesOfDay == activeRepetitiveAlarm.timeMinutesOfDay }.activated)

        collectJob.cancel()
    }


}













































