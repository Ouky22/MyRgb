package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.Esp32
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse
import com.myrgb.ledcontroller.feature.rgbcontroller.Strip
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class DefaultRgbRequestRepositoryTest {
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
        val rgbRequestRepository = DefaultRgbRequestRepository(fakeRgbRequestService)

        val expectedSettingsResponse = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(strip1, strip2), 0
        )

        assertEquals(
            expectedSettingsResponse,
            rgbRequestRepository.loadCurrentRgbSettings(Esp32("test", emptyList()))
        )
    }
}