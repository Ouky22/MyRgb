package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository
import com.myrgb.ledcontroller.persistence.rgbalarm.FakeRgbAlarmDao
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import com.myrgb.ledcontroller.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RgbAlarmAddEditViewModelTest {
    private lateinit var alarmDao: FakeRgbAlarmDao
    private lateinit var repository: DefaultRgbAlarmRepository
    private lateinit var rgbAlarmViewModel: RgbAlarmAddEditViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        alarmDao = FakeRgbAlarmDao()
        repository = DefaultRgbAlarmRepository(alarmDao)
        rgbAlarmViewModel = RgbAlarmAddEditViewModel(repository)
    }

    @Test
    fun `when an alarm is set for editing then the corresponding alarm will be edited`() =
        runTest {
            val rgbAlarm = RgbAlarm(23 * 60, true, RgbTriplet(0, 0, 0))
            alarmDao.insertOrUpdate(rgbAlarm.asEntityDatabaseModel())

            val newTime = 7 * 60 + 59
            rgbAlarmViewModel.setRgbAlarmForEditing(rgbAlarm.timeMinutesOfDay)
            rgbAlarmViewModel.setTime(newTime)
            rgbAlarmViewModel.makeRepetitiveOn(Weekday.SATURDAY)
            rgbAlarmViewModel.saveRgbAlarm()

            val allAlarms = alarmDao.alarmList
            assertEquals(1, allAlarms.size)
            val updatedAlarm = alarmDao.getByTime(newTime).asDomainModel()
            assertTrue(updatedAlarm.activated)
            assertTrue(updatedAlarm.isRepetitiveOn(Weekday.SATURDAY))
        }

    @Test
    fun `when a not existing time is set for editing an alarm then the a new alarm is added`() =
        runTest {
            val initialAlarm = RgbAlarm(23 * 60, false, RgbTriplet(0, 0, 0))
            alarmDao.insertOrUpdate(initialAlarm.asEntityDatabaseModel())

            rgbAlarmViewModel.setRgbAlarmForEditing(2 * 60)
            rgbAlarmViewModel.setTime(13 * 60)
            rgbAlarmViewModel.makeRepetitiveOn(Weekday.SATURDAY)
            rgbAlarmViewModel.saveRgbAlarm()

            val allAlarms = alarmDao.alarmList
            assertEquals(2, allAlarms.size)
            assertTrue(allAlarms.any { it == initialAlarm.asEntityDatabaseModel() })
            val addedAlarm = allAlarms.first { it.timeMinutesOfDay == 13 * 60 }
            assertTrue(addedAlarm.asDomainModel().isRepetitiveOn(Weekday.SATURDAY))
        }

    @Test
    fun `test adding an alarm`() = runTest {
        val initialAlarm = RgbAlarm(0, false, RgbTriplet(0, 100, 0))
        alarmDao.insertOrUpdate(initialAlarm.asEntityDatabaseModel())
        rgbAlarmViewModel.rgbCircleCenterX = 5
        rgbAlarmViewModel.rgbCircleCenterY = 5

        val time = 23 * 60 + 59
        val color = RgbTriplet(255, 0, 0)
        rgbAlarmViewModel.setTime(time)
        rgbAlarmViewModel.makeRepetitiveOn(Weekday.MONDAY)
        rgbAlarmViewModel.makeRepetitiveOn(Weekday.WEDNESDAY)
        rgbAlarmViewModel.makeNotRepetitiveOn(Weekday.WEDNESDAY)
        rgbAlarmViewModel.makeRepetitiveOn(Weekday.SUNDAY)
        rgbAlarmViewModel.setAlarmColorOnRgbCircleTouch(5, 0) // touch is at 0° => color = red
        rgbAlarmViewModel.saveRgbAlarm()

        val alarms = alarmDao.alarmList
        assertEquals(2, alarms.size)
        assertTrue(alarms.any {
            it.asDomainModel() == RgbAlarm(time, false, color).apply {
                makeRepetitiveOn(Weekday.MONDAY)
                makeRepetitiveOn(Weekday.SUNDAY)
            }
        })
    }

    @Test
    fun `when adding an alarm and there is another alarm with the same timeMinutesOfDay then the other alarm will be updated `() =
        runTest {
            rgbAlarmViewModel.rgbCircleCenterX = 5
            rgbAlarmViewModel.rgbCircleCenterY = 5
            val time = 23 * 60
            val alarm = RgbAlarm(time, false, RgbTriplet(0, 0, 0))
            alarmDao.insertOrUpdate(alarm.asEntityDatabaseModel())

            rgbAlarmViewModel.setTime(time)
            rgbAlarmViewModel.makeRepetitiveOn(Weekday.WEDNESDAY)
            rgbAlarmViewModel.setAlarmColorOnRgbCircleTouch(5, 0) // touch at 0° => color = red
            rgbAlarmViewModel.saveRgbAlarm()

            val allAlarms = alarmDao.alarmList
            assertEquals(1, allAlarms.size)
            assertEquals(
                RgbAlarm(time, false, RgbTriplet(255, 0, 0)).apply {
                    makeRepetitiveOn(Weekday.WEDNESDAY)
                },
                allAlarms[0].asDomainModel()
            )
        }
}














































