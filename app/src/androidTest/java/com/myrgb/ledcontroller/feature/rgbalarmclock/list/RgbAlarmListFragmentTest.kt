package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDao
import com.myrgb.ledcontroller.util.DataBindingIdlingResource
import com.myrgb.ledcontroller.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RgbAlarmListFragmentTest {

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
    fun when_clicking_on_rgb_alarm_in_list_then_navigate_to_add_edit_fragment() = runTest {
        val alarm1 = RgbAlarm(10 * 60 + 15, false, RgbTriplet(0, 255, 0))
        val alarm2 = RgbAlarm(10 * 60 + 30, true, RgbTriplet(0, 0, 255))
        rgbAlarmDao.insertOrUpdate(alarm1.asEntityDatabaseModel())
        rgbAlarmDao.insertOrUpdate(alarm2.asEntityDatabaseModel())

        val navController = TestNavHostController(getApplicationContext())
        val fragmentScenario =
            launchFragmentInContainer<RgbAlarmListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)
        fragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_nav_graph)
            navController.setCurrentDestination(R.id.alarm_list_dest)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withText(alarm1.triggerTimeString)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.alarm_add_edit_dest)
    }

    @Test
    fun when_clicking_on_add_fab_then_navigate_to_add_edit_fragment() = runTest {
        val navController = TestNavHostController(getApplicationContext())
        val fragmentScenario =
            launchFragmentInContainer<RgbAlarmListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)
        fragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_nav_graph)
            navController.setCurrentDestination(R.id.alarm_list_dest)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.fab_add_rgb_alarm)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.alarm_add_edit_dest)
    }
}















































