package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDao
import com.myrgb.ledcontroller.util.DataBindingIdlingResource
import com.myrgb.ledcontroller.util.monitorFragment
import com.myrgb.ledcontroller.util.withTint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RgbAlarmAddEditFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Inject
    lateinit var rgbAlarmDao: RgbAlarmDao

    @Before
    fun setupDependencies() {
        (getApplicationContext<TestApp>().appComponent as TestAppComponent).inject(this)
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
    fun when_in_editing_mode_then_the_current_data_of_the_alarm_will_be_displayed() = runTest {
        val alarm = RgbAlarm(10 * 60 + 10, false, RgbTriplet(200, 100, 0)).apply {
            makeRepetitiveOn(Weekday.MONDAY)
            makeRepetitiveOn(Weekday.FRIDAY)
        }
        rgbAlarmDao.insertOrReplace(alarm.asEntityDatabaseModel())

        startFragment(bundleOf("alarm_time" to alarm.timeMinutesOfDay))

        onView(withId(R.id.tv_trigger_time)).check(matches(withText(containsString(alarm.triggerTimeString))))
        onView(withText(getApplicationContext<TestApp>().getString(R.string.monday_start_letter)))
            .check(matches(isChecked()))
        onView(withText(getApplicationContext<TestApp>().getString(R.string.friday_start_letter)))
            .check(matches(isChecked()))
        onView(withText(getApplicationContext<TestApp>().getString(R.string.wednesday_start_letter)))
            .check(matches(not((isChecked()))))
        onView(withId(R.id.iv_alarm_clock)).check(matches(withTint(alarm.color)))
    }

    private fun startFragment(fragmentArgs: Bundle = bundleOf()): FragmentScenario<RgbAlarmAddEditFragment> {
        val fragmentScenario = FragmentScenario.launchInContainer(
            fragmentClass = RgbAlarmAddEditFragment::class.java,
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_LedController
        )
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        return fragmentScenario
    }
}







































