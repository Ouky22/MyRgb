package com.myrgb.ledcontroller.persistence.rgbalarm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.LedControllerDatabase
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RgbAlarmDaoTest {

    private lateinit var rgbAlarmDao: RgbAlarmDao
    private lateinit var database: LedControllerDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LedControllerDatabase::class.java).build()
        rgbAlarmDao = database.rgbAlarmDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun test_getAllActiveAlarms() = runTest {
        val alarm1 = RgbAlarm(8 * 60, true, RgbTriplet(0, 0, 0))
        val alarm2 = RgbAlarm(12 * 60, false, RgbTriplet(0, 0, 0))
        val alarm3 = RgbAlarm(18 * 60, false, RgbTriplet(0, 0, 0))
        val alarm4 = RgbAlarm(9 * 60, true, RgbTriplet(0, 0, 0))

        rgbAlarmDao.insertOrReplace(
            listOf(
                alarm1.asEntityDatabaseModel(),
                alarm2.asEntityDatabaseModel(),
                alarm3.asEntityDatabaseModel(),
                alarm4.asEntityDatabaseModel(),
            )
        )

        val allActiveAlarms = rgbAlarmDao.getAllActiveAlarms()
        assertEquals(2, allActiveAlarms.size)
        assertTrue(allActiveAlarms.any { it == alarm1.asEntityDatabaseModel() })
        assertTrue(allActiveAlarms.any { it == alarm4.asEntityDatabaseModel() })
    }

    @Test
    fun when_adding_existing_and_new_alarms_the_existing_alarms_are_updated_and_the_new_alarms_are_added() =
        runTest {
            var alarm1 = RgbAlarm(8 * 60, true, RgbTriplet(0, 0, 0))
            val alarm2 = RgbAlarm(12 * 60, false, RgbTriplet(0, 0, 0))
            val alarm3 = RgbAlarm(18 * 60, false, RgbTriplet(0, 0, 0))
            rgbAlarmDao.insertOrReplace(alarm1.asEntityDatabaseModel())
            alarm1 = RgbAlarm(8 * 60, false, RgbTriplet(32, 0, 10))

            rgbAlarmDao.insertOrReplace(
                listOf(
                    alarm1.asEntityDatabaseModel(), // gets updated
                    alarm2.asEntityDatabaseModel(), // gets added
                    alarm3.asEntityDatabaseModel(), // gets added
                )
            )

            val allAlarms = rgbAlarmDao.observeAllAlarmsSortedByTime().first()
            assertEquals(3, allAlarms.size)
            assertTrue(allAlarms.any { it == alarm1.asEntityDatabaseModel() })
            assertTrue(allAlarms.any { it == alarm2.asEntityDatabaseModel() })
            assertTrue(allAlarms.any { it == alarm3.asEntityDatabaseModel() })
        }

    @Test
    fun when_activating_active_alarm_then_it_is_still_activated() = runTest {
        val alarm1 = RgbAlarm(5 * 60, true, RgbTriplet(0, 0, 0))
        rgbAlarmDao.insertOrReplace(alarm1.asEntityDatabaseModel())

        rgbAlarmDao.activateRgbAlarmByTime(alarm1.timeMinutesOfDay, 0)

        val rgbAlarmFromDb = rgbAlarmDao.getByTime(alarm1.timeMinutesOfDay)
        assertTrue(rgbAlarmFromDb.activated)
    }

    @Test
    fun when_activating_not_active_alarm_then_it_is_activated() = runTest {
        val alarm1 = RgbAlarm(5 * 60, false, RgbTriplet(0, 0, 0))
        rgbAlarmDao.insertOrReplace(alarm1.asEntityDatabaseModel())

        rgbAlarmDao.activateRgbAlarmByTime(alarm1.timeMinutesOfDay, 0)

        val rgbAlarmFromDb = rgbAlarmDao.getByTime(alarm1.timeMinutesOfDay)
        assertTrue(rgbAlarmFromDb.activated)
    }

    @Test
    fun when_deactivating_not_active_alarm_then_it_is_still_deactivated() = runTest {
        val alarm1 = RgbAlarm(5 * 60, false, RgbTriplet(0, 0, 0))
        rgbAlarmDao.insertOrReplace(alarm1.asEntityDatabaseModel())

        rgbAlarmDao.deactivateRgbAlarmByTime(alarm1.timeMinutesOfDay)

        val rgbAlarmFromDb = rgbAlarmDao.getByTime(alarm1.timeMinutesOfDay)
        assertFalse(rgbAlarmFromDb.activated)
    }

    @Test
    fun when_deactivating_active_alarm_then_it_is_deactivated() = runTest {
        val alarm1 = RgbAlarm(5 * 60, true, RgbTriplet(0, 0, 0))
        rgbAlarmDao.insertOrReplace(alarm1.asEntityDatabaseModel())

        rgbAlarmDao.deactivateRgbAlarmByTime(alarm1.timeMinutesOfDay)

        val rgbAlarmFromDb = rgbAlarmDao.getByTime(alarm1.timeMinutesOfDay)
        assertFalse(rgbAlarmFromDb.activated)
    }

    @Test
    fun when_activating_non_existing_alarm_nothing_changes() = runTest {
        rgbAlarmDao.activateRgbAlarmByTime(42, 0)

        assertTrue(rgbAlarmDao.observeAllAlarmsSortedByTime().first().isEmpty())
    }

    @Test
    fun when_deactivating_non_existing_alarm_nothing_changes() = runTest {
        rgbAlarmDao.deactivateRgbAlarmByTime(42)

        assertTrue(rgbAlarmDao.observeAllAlarmsSortedByTime().first().isEmpty())
    }
}







































