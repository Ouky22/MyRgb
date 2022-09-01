package com.myrgb.ledcontroller.feature.rgbcontroller

import com.myrgb.ledcontroller.network.FakeRgbRequestService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class DefaultControllerRepositoryTest {

    @ExperimentalCoroutinesApi
    @Test
    fun testGetCurrentSettings() = runTest {
        val strip1 = Strip("strip1", 1)
        val strip2 = Strip("strip2", 1)
        val rgbSettingsResponse = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip1, strip2), 0
        )
        val fakeRgbRequestService = FakeRgbRequestService(rgbSettingsResponse)
        val defaultControllerRepository =
            DefaultControllerRepository(fakeRgbRequestService)

        val expectedSettingsResponse = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip1, strip2, strip1, strip2), 0
        )

        assertEquals(expectedSettingsResponse, defaultControllerRepository.getCurrentSettings())
    }
}