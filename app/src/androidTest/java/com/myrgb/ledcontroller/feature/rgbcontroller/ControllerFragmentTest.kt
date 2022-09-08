package com.myrgb.ledcontroller.feature.rgbcontroller

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import com.myrgb.ledcontroller.network.RgbSettingsResponse
import com.myrgb.ledcontroller.persistence.FakeIpAddressStorage
import com.myrgb.ledcontroller.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ControllerFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Inject
    lateinit var fakeIpAddressStorage: FakeIpAddressStorage

    @Inject
    lateinit var fakeRgbRequestService: FakeRgbRequestService

    private val fakeIpAddress1 = "192.168.1.1"
    private val fakeIpAddress2 = "192.168.1.2"

    @Before
    fun setupDependencies() {
        (getApplicationContext<TestApp>().appComponent as TestAppComponent).inject(this)
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun clearAddressStorage() {
        fakeIpAddressStorage.deleteAllIpAddresses()
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun ui_displays_all_strip_buttons_if_there_are_many_strips() {
        val rgbStrip1 = RgbStrip(1, "strip1", false)
        val rgbStrip2 = RgbStrip(2, "strip2", true)
        val rgbStrip3 = RgbStrip(3, "strip3", false)
        val rgbStrip4 = RgbStrip(4, "strip4", true)
        val rgbStrip5 = RgbStrip(5, "strip5", true)
        val rgbStrip6 = RgbStrip(6, "strip6", true)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 132, 255), 150, false,
            listOf(rgbStrip1, rgbStrip2, rgbStrip3, rgbStrip4, rgbStrip5, rgbStrip6)
        )
        fakeIpAddressStorage.addIpAddress(fakeIpAddress1)
        fakeRgbRequestService.setIpAddressRgbSettingsMap(
            hashMapOf(Pair(fakeIpAddress1, rgbSettings))
        )

        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withText(rgbStrip1.name)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.scroll_view_buttons)).perform(swipeLeft())
        onView(withText(rgbStrip6.name)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun ui_reflects_basic_rgb_settings() {
        val rgbStrip1 = RgbStrip(1, "strip1", false)
        val rgbStrip2 = RgbStrip(2, "strip2", true)
        val rgbStrip3 = RgbStrip(3, "strip3", false)
        val rgbSettings1 = RgbSettingsResponse(
            RgbTriplet(0, 132, 255), 150, false,
            listOf(rgbStrip1, rgbStrip2)
        )
        val rgbSettings2 = RgbSettingsResponse(
            RgbTriplet(0, 132, 255), 150, false,
            listOf(rgbStrip3)
        )
        fakeIpAddressStorage.addIpAddress(fakeIpAddress1)
        fakeIpAddressStorage.addIpAddress(fakeIpAddress2)
        fakeRgbRequestService.setIpAddressRgbSettingsMap(
            hashMapOf(Pair(fakeIpAddress1, rgbSettings1), Pair(fakeIpAddress2, rgbSettings2))
        )

        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        // check color of bulb imageView
        onView(withId(R.id.iv_bulb)).check(matches(withTint(rgbSettings1.color)))

        // check progress of brightness seekBar
        val expectedProgress = rgbSettings1.brightness / 10
        onView(withId(R.id.seekBar_brightness)).check(matches(withSeekbarProgress(expectedProgress)))

        // check stroke color of buttons
        onView(withText(rgbStrip1.name)).check(matches(withStrokeColor(R.color.btn_color_off)))
        onView(withText(rgbStrip2.name)).check(matches(withStrokeColor(R.color.btn_color_on)))
        onView(withText(rgbStrip3.name)).check(matches(withStrokeColor(R.color.btn_color_off)))
    }

    @Test
    fun when_strip_is_enabled_then_its_corresponding_button_has_green_border(): Unit = runBlocking {
        val rgbStrip = RgbStrip(1, "strip1", false)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 132, 255), 150, false, listOf(rgbStrip)
        )
        fakeIpAddressStorage.addIpAddress(fakeIpAddress1)
        fakeRgbRequestService.setIpAddressRgbSettingsMap(
            hashMapOf(Pair(fakeIpAddress1, rgbSettings))
        )

        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withText(rgbStrip.name)).perform(click())

        onView(withText(rgbStrip.name)).check(matches(withStrokeColor(R.color.btn_color_on)))
    }

    @Test
    fun when_strip_is_disabled_then_its_corresponding_button_has_red_border(): Unit = runBlocking {
        val rgbStrip1 = RgbStrip(1, "strip1", true)
        val rgbStrip2 = RgbStrip(2, "strip2", true)
        val rgbStrip3 = RgbStrip(3, "strip3", false)
        val rgbStrip4 = RgbStrip(4, "strip4", true)
        val rgbStrip5 = RgbStrip(5, "strip5", true)
        val rgbStrip6 = RgbStrip(6, "strip6", true)
        val rgbSettings = RgbSettingsResponse(
            RgbTriplet(0, 132, 255), 150, false,
            listOf(rgbStrip1, rgbStrip2, rgbStrip3, rgbStrip4, rgbStrip5, rgbStrip6)
        )
        fakeIpAddressStorage.addIpAddress(fakeIpAddress1)
        fakeRgbRequestService.setIpAddressRgbSettingsMap(
            hashMapOf(Pair(fakeIpAddress1, rgbSettings))
        )

        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.scroll_view_buttons)).perform(swipeLeft())
        onView(withText(rgbStrip6.name)).perform(click())

        onView(withText(rgbStrip6.name)).check(matches(withStrokeColor(R.color.btn_color_off)))
    }

    @Test
    fun touch_on_rgb_circle_image_view_at_0_degrees_should_display_right_color_in_bulb_image_view(): Unit =
        runBlocking {
            val fragmentScenario =
                launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
            dataBindingIdlingResource.monitorFragment(fragmentScenario)

            onView(withId(R.id.iv_rgb_circle)).perform(
                relativeClickAt(fragmentScenario.getWidthOfView(R.id.iv_rgb_circle) / 2, 10F)
            )
            val expectedTint = RgbCircle().calculateColorAtAngle(0)
            onView(withId(R.id.iv_bulb)).check(matches(withTint(expectedTint)))
        }

    @Test
    fun touch_on_rgb_circle_image_view_at_180_degrees_should_display_right_color_in_bulb_image_view(): Unit =
        runBlocking {
            val fragmentScenario =
                launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
            dataBindingIdlingResource.monitorFragment(fragmentScenario)

            val rgbCircleHeight = fragmentScenario.getHeightOfView(R.id.iv_rgb_circle)
            val rgbCircleWidth = fragmentScenario.getWidthOfView(R.id.iv_rgb_circle)

            onView(withId(R.id.iv_rgb_circle)).perform(
                relativeClickAt((rgbCircleWidth / 2), rgbCircleHeight - 10)
            )
            val expectedTint = RgbCircle().calculateColorAtAngle(180)
            onView(withId(R.id.iv_bulb)).check(matches(withTint(expectedTint)))
        }

    @Test
    fun touch_on_rgb_circle_image_view_at_270_degrees_should_display_right_color_in_bulb_image_view(): Unit =
        runBlocking {
            val fragmentScenario =
                launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
            dataBindingIdlingResource.monitorFragment(fragmentScenario)

            val rgbCircleHeight = fragmentScenario.getHeightOfView(R.id.iv_rgb_circle)

            onView(withId(R.id.iv_rgb_circle)).perform(
                relativeClickAt(0F, rgbCircleHeight / 2)
            )
            val expectedTint = RgbCircle().calculateColorAtAngle(270)
            onView(withId(R.id.iv_bulb)).check(matches(withTint(expectedTint)))
        }


    private fun FragmentScenario<ControllerFragment>.getHeightOfView(@IdRes id: Int): Float {
        var viewHeight = 0
        onFragment {
            val view = it.activity?.findViewById<View>(id)
            viewHeight = view?.height ?: 0
        }
        return viewHeight.toFloat()
    }

    private fun FragmentScenario<ControllerFragment>.getWidthOfView(@IdRes id: Int): Float {
        var viewWidth = 0
        onFragment {
            val view = it.activity?.findViewById<View>(id)
            viewWidth = view?.width ?: 0
        }
        return viewWidth.toFloat()
    }
}









































