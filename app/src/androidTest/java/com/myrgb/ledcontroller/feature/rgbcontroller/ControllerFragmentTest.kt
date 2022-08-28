package com.myrgb.ledcontroller.feature.rgbcontroller

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.di.ControllerContainer
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import com.myrgb.ledcontroller.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ControllerFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setupControllerRepository() {
        (getApplicationContext() as App).appContainer.controllerContainer =
            ControllerContainer(FakeControllerRepository(FakeRgbRequestService()))
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun when_loading_current_rgb_settings_then_UI_reflects_them() {
        val currentRgbSettings = RgbSettingsResponse(
            0, 0, 100, 255, 150,
            listOf(Strip("desk", 0), Strip("sofa", 1), Strip("bed", 1)), 0
        )
        // set currentRgbSettings
        (getApplicationContext() as App).appContainer.controllerContainer =
            ControllerContainer(FakeControllerRepository(FakeRgbRequestService(currentRgbSettings)))

        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        // check color of bulb imageView
        val expectedTint = RgbTriplet(
            currentRgbSettings.redValue,
            currentRgbSettings.greenValue,
            currentRgbSettings.blueValue
        )
        onView(withId(R.id.iv_bulb)).check(matches(withTint(expectedTint)))

        // check progress of brightness seekBar
        val expectedProgress = currentRgbSettings.brightness / 10
        onView(withId(R.id.seekBar_brightness)).check(matches(withSeekbarProgress(expectedProgress)))

        // check stroke color of buttons
        onView(withId(R.id.btn_desk)).check(matches(withStrokeColor(R.color.btn_color_off)))
        onView(withId(R.id.btn_sofa)).check(matches(withStrokeColor(R.color.btn_color_on)))
        onView(withId(R.id.btn_bed)).check(matches(withStrokeColor(R.color.btn_color_on)))
    }

    @Test
    fun when_strip_is_enabled_then_its_corresponding_button_has_green_border() {
        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_bed)).perform(click())

        onView(withId(R.id.btn_bed)).check(matches(withStrokeColor(R.color.btn_color_on)))
    }

    @Test
    fun when_strip_is_disabled_then_its_corresponding_button_has_red_border() {
        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_bed)).perform(click())
        onView(withId(R.id.btn_bed)).perform(click())

        onView(withId(R.id.btn_bed)).check(matches(withStrokeColor(R.color.btn_color_off)))
    }

    @Test
    fun touch_on_rgb_circle_image_view_at_0_degrees_should_display_right_color_in_bulb_image_view() {
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
    fun touch_on_rgb_circle_image_view_at_180_degrees_should_display_right_color_in_bulb_image_view() {
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
    fun touch_on_rgb_circle_image_view_at_270_degrees_should_display_right_color_in_bulb_image_view() {
        val fragmentScenario =
            launchFragmentInContainer<ControllerFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        val rgbCircleHeight = fragmentScenario.getHeightOfView(R.id.iv_rgb_circle)
        val rgbCircleWidth = fragmentScenario.getWidthOfView(R.id.iv_rgb_circle)

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









































