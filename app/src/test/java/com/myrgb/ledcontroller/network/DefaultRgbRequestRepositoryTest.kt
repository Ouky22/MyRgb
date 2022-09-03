package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultRgbRequestRepositoryTest {

    @Test
    fun `get a successful current settings response`() = runTest {
        val strip1 = RgbStrip(1, "strip1", false)
        val strip2 = RgbStrip(2, "strip2", true)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 255, 0), 100,
            true, listOf(strip1, strip2)
        )
        val fakeRgbRequestService = FakeRgbRequestService(rgbSettings)
        val rgbRequestRepository = DefaultRgbRequestRepository(fakeRgbRequestService)

        assertEquals(
            rgbSettings,
            rgbRequestRepository.loadCurrentRgbSettings("test")
        )
    }

    @Test
    fun `get a unsuccessful current settings response`() = runTest {
        val strip1 = RgbStrip(1, "strip1", false)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 255, 0), 100,
            true, listOf(strip1)
        )
        val fakeRgbRequestService = FakeRgbRequestService(rgbSettings)
        fakeRgbRequestService.loadingSettingsThrowsIoException = true
        val rgbRequestRepository = DefaultRgbRequestRepository(fakeRgbRequestService)

        assertEquals(
            null,
            rgbRequestRepository.loadCurrentRgbSettings("test")
        )
    }
}