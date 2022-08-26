package com.myrgb.ledcontroller.feature.rgbcontroller

import com.myrgb.ledcontroller.network.FakeRgbRequestService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ControllerRepositoryTest {

    @ExperimentalCoroutinesApi
    @Test
    fun testGetCurrentSettings() = runTest {
        val strip1 = Strip("strip1", 1)
        val strip2 = Strip("strip2", 1)
        val strip3 = Strip("strip3", 0)
        val rgbSettingsResponse1 = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip1), 0
        )
        val rgbSettingsResponse2 = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip2, strip3), 0
        )
        val fakeRgbRequestService1 = FakeRgbRequestService(rgbSettingsResponse1)
        val fakeRgbRequestService2 = FakeRgbRequestService(rgbSettingsResponse2)
        val controllerRepository =
            ControllerRepository(fakeRgbRequestService1, fakeRgbRequestService2)


        val expectedCurrentRgbSettings = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip1, strip2, strip3), 0
        )
        assertEquals(expectedCurrentRgbSettings, controllerRepository.getCurrentSettings())
    }
}