package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.MainDispatcherRule
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.getOrAwaitValue
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ControllerViewModelTest {
    private lateinit var controllerRepository: ControllerRepository
    private lateinit var viewModel: ControllerViewModel
    private lateinit var currentRgbSettings: RgbSettingsResponse

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        currentRgbSettings = RgbSettingsResponse(
            0, 0, 0, 0, 0,
            listOf(Strip("strip1", 0)), 0
        )
        controllerRepository = FakeControllerRepository(FakeRgbRequestService(currentRgbSettings))
        viewModel = ControllerViewModel(controllerRepository)
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
        runTest(
            UnconfinedTestDispatcher()
        ) {
            viewModel.rgbCircleCenterX = 0
            viewModel.rgbCircleCenterY = 0
            viewModel.onRgbCircleTouch(0, 1)

            val currentColor = viewModel.currentlySelectedColor.getOrAwaitValue()
            assertEquals(RgbTriplet(0, 255, 255), currentColor)
        }

    @Test
    fun `when all strips are off and click on rgb bulb then all strips are turned on`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onRgbBulbButtonClick()
            assertTrue(viewModel.isDeskLedStripOn.getOrAwaitValue())
            assertTrue(viewModel.isBedLedStripOn.getOrAwaitValue())
            assertTrue(viewModel.isSofaLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when all strips are on and click on rgb bulb then all strips are turned off`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonBedClick()
            viewModel.onButtonSofaClick()
            viewModel.onButtonBedClick()

            viewModel.onRgbBulbButtonClick()

            assertFalse(viewModel.isDeskLedStripOn.getOrAwaitValue())
            assertFalse(viewModel.isBedLedStripOn.getOrAwaitValue())
            assertFalse(viewModel.isSofaLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when only one strip is on and click on rgb bulb then all strips are turned off`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonSofaClick()

            viewModel.onRgbBulbButtonClick()

            assertFalse(viewModel.isDeskLedStripOn.getOrAwaitValue())
            assertFalse(viewModel.isBedLedStripOn.getOrAwaitValue())
            assertFalse(viewModel.isSofaLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when sofa strip is off and sofa button clicked then sofa strip is on`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonSofaClick()
            assertTrue(viewModel.isSofaLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when sofa strip is on and sofa button clicked then sofa strip is off`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonSofaClick()

            viewModel.onButtonSofaClick()
            assertFalse(viewModel.isSofaLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when bed strip is off and bed button clicked then bed strip is on`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonBedClick()
            assertTrue(viewModel.isBedLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when bed strip is on and bed button clicked then bed strip is off`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonBedClick()

            viewModel.onButtonBedClick()
            assertFalse(viewModel.isBedLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when desk strip is off and desk button clicked then desk strip is on`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonDeskClick()
            assertTrue(viewModel.isDeskLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when desk strip is on and desk button clicked then desk strip is off`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onButtonDeskClick()

            viewModel.onButtonDeskClick()
            assertFalse(viewModel.isDeskLedStripOn.getOrAwaitValue())
        }

    @Test
    fun `when minimum brightness is selected then current brightness should be the min brightness`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onBrightnessSeekBarProgressChanged(0)

            assertEquals(
                viewModel.minBrightness,
                viewModel.currentlySelectedBrightness.getOrAwaitValue()
            )
        }

    @Test
    fun `when maximum brightness is selected then current brightness should be the max brightness`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.onBrightnessSeekBarProgressChanged(viewModel.maxBrightness / 10)

            assertEquals(
                viewModel.maxBrightness,
                viewModel.currentlySelectedBrightness.getOrAwaitValue()
            )
        }

    @Test
    fun testLoadingCurrentSettings() = runTest(UnconfinedTestDispatcher()) {
        viewModel.loadCurrentSettings()

        val expectedCurrentRgbColor = RgbTriplet(
            currentRgbSettings.redValue,
            currentRgbSettings.greenValue,
            currentRgbSettings.blueValue
        )
        assertEquals(expectedCurrentRgbColor, viewModel.currentlySelectedColor.getOrAwaitValue())
        assertEquals(currentRgbSettings.brightness, viewModel.currentlySelectedBrightness.getOrAwaitValue())
        assertFalse(viewModel.isSofaLedStripOn.getOrAwaitValue())
        assertFalse(viewModel.isBedLedStripOn.getOrAwaitValue())
        assertFalse(viewModel.isDeskLedStripOn.getOrAwaitValue())
    }
}

























