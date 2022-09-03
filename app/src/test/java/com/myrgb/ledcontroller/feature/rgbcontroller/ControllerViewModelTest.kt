package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.MainDispatcherRule
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.getOrAwaitValue
import com.myrgb.ledcontroller.network.*
import com.myrgb.ledcontroller.persistence.FakeIpAddressStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ControllerViewModelTest {
    private lateinit var viewModel: ControllerViewModel
    private lateinit var fakeIpAddressStorage: FakeIpAddressStorage

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        val currentRgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 0, 0), 0, false, emptyList()
        )
        val rgbRequestRepository =
            FakeRgbRequestRepository(FakeRgbRequestService(currentRgbSettings))
        fakeIpAddressStorage = FakeIpAddressStorage()
        viewModel = ControllerViewModel(rgbRequestRepository, fakeIpAddressStorage)
    }

    private fun setCurrentRgbSettings(currentRgbSettings: RgbSettingsResponse) {
        val rgbRequestRepository =
            FakeRgbRequestRepository(FakeRgbRequestService(currentRgbSettings))
        viewModel = ControllerViewModel(rgbRequestRepository, fakeIpAddressStorage)
    }

    @Test
    fun `when touch is at 0 degrees then currently selected color should be red`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.rgbCircleCenterX = 5
            viewModel.rgbCircleCenterY = 5
            viewModel.onRgbCircleTouch(5, 3)

            val currentColor = viewModel.currentlySelectedColor.getOrAwaitValue()
            assertEquals(RgbTriplet(255, 0, 0), currentColor)
        }

    @Test
    fun `when touch is at 180 degrees then currently selected color should be (0,255,255)`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.rgbCircleCenterX = 0
            viewModel.rgbCircleCenterY = 0
            viewModel.onRgbCircleTouch(0, 1)

            val currentColor = viewModel.currentlySelectedColor.getOrAwaitValue()
            assertEquals(RgbTriplet(0, 255, 255), currentColor)
        }

    @Test
    fun `when all strips are off and click on rgb bulb then all strips are turned on`() =
        runTest(UnconfinedTestDispatcher()) {
            val currentRgbSettings = RgbSettingsResponse(
                RgbTriplet(0, 0, 0), 0, false,
                listOf(RgbStrip(1, "s1", false), RgbStrip(2, "s2", false))
            )
            setCurrentRgbSettings(currentRgbSettings)

            viewModel.onRgbBulbButtonClick()

            viewModel.rgbStrips.getOrAwaitValue().forEach { strip ->
                assertTrue(strip.enabled)
            }
        }

    @Test
    fun `when all strips are on and click on rgb bulb then all strips are turned off`() =
        runTest(UnconfinedTestDispatcher()) {
            val currentRgbSettings = RgbSettingsResponse(
                RgbTriplet(0, 50, 0), 0, true,
                listOf(RgbStrip(10, "s1", true), RgbStrip(20, "s2", true))
            )
            setCurrentRgbSettings(currentRgbSettings)

            viewModel.onRgbBulbButtonClick()

            viewModel.rgbStrips.getOrAwaitValue().forEach { strip ->
                assertFalse(strip.enabled)
            }
        }

    @Test
    fun `when only one strip is on and click on rgb bulb then all strips are turned off`() =
        runTest(UnconfinedTestDispatcher()) {
            val currentRgbSettings = RgbSettingsResponse(
                RgbTriplet(70, 10, 0), 0, false,
                listOf(RgbStrip(9, "s1", true), RgbStrip(3, "s2", false))
            )
            setCurrentRgbSettings(currentRgbSettings)

            viewModel.onRgbBulbButtonClick()

            viewModel.rgbStrips.getOrAwaitValue().forEach { strip ->
                assertFalse(strip.enabled)
            }
        }

    @Test
    fun `when minimum brightness is selected then current brightness should be the min brightness`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onBrightnessSeekBarProgressChanged(0, true)

            assertEquals(
                viewModel.minBrightness,
                viewModel.currentlySelectedBrightness.getOrAwaitValue()
            )
        }

    @Test
    fun `when maximum brightness is selected then current brightness should be the max brightness`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onBrightnessSeekBarProgressChanged(viewModel.maxBrightness / 10, true)

            assertEquals(
                viewModel.maxBrightness,
                viewModel.currentlySelectedBrightness.getOrAwaitValue()
            )
        }

    @Test
    fun testLoadingCurrentSettings() = runTest(UnconfinedTestDispatcher()) {
        val strip1 = RgbStrip(1, "s1", false)
        val strip2 = RgbStrip(2, "s2", true)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 147, 255), 17, false,
            listOf(strip1, strip2)
        )
        setCurrentRgbSettings(rgbSettings)

        fakeIpAddressStorage.addIpAddress("192.168.1.1")
        viewModel.loadCurrentSettings()

        assertEquals(rgbSettings.color, viewModel.currentlySelectedColor.getOrAwaitValue())
        assertEquals(
            rgbSettings.brightness, viewModel.currentlySelectedBrightness.getOrAwaitValue()
        )
        val rgbStrips = viewModel.rgbStrips.getOrAwaitValue()
        assertTrue(rgbStrips.contains(strip1))
        assertTrue(rgbStrips.contains(strip2))
        assertEquals(strip1.enabled, rgbStrips.firstOrNull { it.name == strip1.name }?.enabled)
        assertEquals(strip2.enabled, rgbStrips.firstOrNull { it.name == strip2.name }?.enabled)
    }
}

























