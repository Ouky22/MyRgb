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
import java.time.*

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
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.alarms.toList(emittedValues)
        }

        // set clock to 9:00 am
        val testDateTime = LocalDateTime.of(2023, 12, 31, 9, 0)
        RgbAlarm.clock = Clock.fixed(
            Instant.ofEpochSecond(testDateTime.toEpochSecond(ZoneOffset.UTC)),
            ZoneId.of("UTC")
        )
        // activate alarms between 10:00 and 10:30 am
        val activeOneTimeAlarm1 = RgbAlarm(10 * 60, true, RgbTriplet(0, 0, 0), testDateTime)
        val activeOneTimeAlarm2 = RgbAlarm(11 * 60, true, RgbTriplet(0, 0, 0), testDateTime)
        val disabledOneTimeAlarm = RgbAlarm(10 * 60 + 15, false, RgbTriplet(0, 0, 0), testDateTime)
        val activeRepetitiveAlarm =
            RgbAlarm(10 * 60 + 30, true, RgbTriplet(0, 0, 0), testDateTime).apply {
                makeRepetitiveOn(Weekday.MONDAY)
            }
        fakeRgbAlarmDao.insertOrReplace(activeOneTimeAlarm1.asEntityDatabaseModel())
        fakeRgbAlarmDao.insertOrReplace(activeOneTimeAlarm2.asEntityDatabaseModel())
        fakeRgbAlarmDao.insertOrReplace(disabledOneTimeAlarm.asEntityDatabaseModel())
        fakeRgbAlarmDao.insertOrReplace(activeRepetitiveAlarm.asEntityDatabaseModel())
        // set clock to 11.00
        RgbAlarm.clock = Clock.fixed(
            Instant.ofEpochSecond(testDateTime.plusHours(2).toEpochSecond(ZoneOffset.UTC)),
            ZoneId.of("UTC")
        )

        repository = RgbAlarmRepository(fakeRgbAlarmDao)

        // repository should receive the alarms..
        fakeRgbAlarmDao.emitAlarms()
        fakeRgbAlarmDao.emitAlarms() // retrieve alarms again after they got refreshed by AlarmRepository
        // ..and disables expired one-time alarms
        assertFalse(
            emittedValues.last()
                .first { it.timeMinutesOfDay == activeOneTimeAlarm1.timeMinutesOfDay }.activated
        )
        assertFalse(
            emittedValues.last()
                .first { it.timeMinutesOfDay == activeOneTimeAlarm2.timeMinutesOfDay }.activated
        )
        assertFalse(
            emittedValues.last()
                .first { it.timeMinutesOfDay == disabledOneTimeAlarm.timeMinutesOfDay }.activated
        )
        assertTrue(
            emittedValues.last()
                .first { it.timeMinutesOfDay == activeRepetitiveAlarm.timeMinutesOfDay }.activated
        )

        collectJob.cancel()
    }

    @Test
    fun `when there is no active alarm then getNextActiveAlarm returns null`() = runTest {
        val deactivatedAlarm = RgbAlarm(0, false, RgbTriplet(0, 0, 0))
        repository.insertOrReplace(deactivatedAlarm)
        assertNull(repository.getNextActiveAlarm())
    }

    @Test
    fun `when there are active alarms then getNextActiveAlarm returns the next alarm`() = runTest {
        // set clock to 9:00 am
        val testDateTime = LocalDateTime.of(2023, 12, 31, 9, 0)
        RgbAlarm.clock = Clock.fixed(
            Instant.ofEpochSecond(testDateTime.toEpochSecond(ZoneOffset.UTC)),
            ZoneId.of("UTC")
        )
        val deactivatedAlarm1 = RgbAlarm(7 * 60, false, RgbTriplet(0, 0, 0), testDateTime)
        val deactivatedAlarm2 = RgbAlarm(11 * 60 + 30, false, RgbTriplet(0, 0, 0), testDateTime)
        val activatedAlarm1 = RgbAlarm(12 * 60, true, RgbTriplet(0, 0, 0), testDateTime)
        val activatedAlarm2 = RgbAlarm(10 * 60, true, RgbTriplet(0, 0, 0), testDateTime).apply {
            makeRepetitiveOn(Weekday.FRIDAY)
        }
        repository.insertOrReplace(deactivatedAlarm1)
        repository.insertOrReplace(deactivatedAlarm2)
        repository.insertOrReplace(activatedAlarm1)
        repository.insertOrReplace(activatedAlarm2)

        // set clock to 11.00
        RgbAlarm.clock = Clock.fixed(
            Instant.ofEpochSecond(testDateTime.plusHours(2).toEpochSecond(ZoneOffset.UTC)),
            ZoneId.of("UTC")
        )

        assertEquals(activatedAlarm1, repository.getNextActiveAlarm())
    }
}













































