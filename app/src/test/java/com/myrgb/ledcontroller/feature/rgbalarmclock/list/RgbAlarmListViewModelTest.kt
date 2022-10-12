package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.rgbalarm.FakeRgbAlarmDao
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import com.myrgb.ledcontroller.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RgbAlarmListViewModelTest {
    private lateinit var alarmDao: FakeRgbAlarmDao
    private lateinit var repository: RgbAlarmRepository
    private lateinit var rgbAlarmListViewModel: RgbAlarmListViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        alarmDao = FakeRgbAlarmDao()
        repository = RgbAlarmRepository(alarmDao)
        rgbAlarmListViewModel = RgbAlarmListViewModel(repository)
    }

    @Test
    fun `delete multiple alarms`() = runTest {
        val rgbAlarm1 = RgbAlarm(13 * 60, true, RgbTriplet(0, 0, 0))
        val rgbAlarm2 = RgbAlarm(10 * 60, false, RgbTriplet(10, 0, 0))
        alarmDao.insertOrIgnore(
            listOf(
                rgbAlarm1.asEntityDatabaseModel(), rgbAlarm2.asEntityDatabaseModel()
            )
        )
        assertEquals(2, alarmDao.alarmList.size)

        rgbAlarmListViewModel.deleteRgbAlarms(listOf(rgbAlarm1, rgbAlarm2))

        assertEquals(0, alarmDao.alarmList.size)
    }

    @Test
    fun `try to delete alarms when there are no alarms`() = runBlocking {
        val rgbAlarm1 = RgbAlarm(13 * 60, true, RgbTriplet(0, 0, 0))
        val rgbAlarm2 = RgbAlarm(10 * 60, false, RgbTriplet(10, 0, 0))
        rgbAlarmListViewModel.deleteRgbAlarms(listOf(rgbAlarm1, rgbAlarm2))

        assertEquals(0, alarmDao.alarmList.size)
    }
}