package com.myrgb.ledcontroller.feature.ipsettings

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.persistence.ipaddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.extensions.DataBindingIdlingResource
import com.myrgb.ledcontroller.extensions.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class IpAddressListFragmentTest {
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Inject
    lateinit var fakeIpAddressSettingsRepository: FakeIpAddressSettingsRepository

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

    private fun createIpAddressSettings(
        ipAddresses: List<String>,
        name: String = "test"
    ): IpAddressSettings {
        val ipAddressNamePairs = ipAddresses.map {
            IpAddressNamePair.newBuilder().setIpAddress(it).setName(name).build()
        }
        return IpAddressSettings.newBuilder().addAllIpAddressNamePairs(ipAddressNamePairs).build()
    }


    @Test
    fun when_there_are_stored_ip_addresses_then_they_are_displayed() = runBlocking<Unit> {
        startFragment()

        val ip1 = "192.168.1.1"
        val ip2 = "192.168.1.2"
        val ip3 = "192.168.1.3"
        val currentIpAddressSettings = createIpAddressSettings(listOf(ip1, ip2, ip3))
        fakeIpAddressSettingsRepository.emit(currentIpAddressSettings)

        onView(withText(ip1)).check(matches(isDisplayed()))
        onView(withText(ip2)).check(matches(isDisplayed()))
        onView(withText(ip3)).check(matches(isDisplayed()))
    }

    @Test
    fun when_click_on_delete_ip_button_then_dialog_asks_before_deleting_ip() = runBlocking<Unit> {
        startFragment()

        val ip = "192.168.1.1"
        val ipName = "test"
        fakeIpAddressSettingsRepository.emit(createIpAddressSettings(listOf(ip), ipName))

        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(
            withText(
                getApplicationContext<Context>().getString(
                    R.string.are_sure_to_delete,
                    "\"$ipName\" ($ip)"
                )
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun when_ip_address_deleted_then_it_is_no_longer_displayed() = runBlocking<Unit> {
        startFragment()

        val ip = "192.168.1.1"
        val ipName = "test"
        fakeIpAddressSettingsRepository.emit(createIpAddressSettings(listOf(ip), ipName))

        onView(withText(ip)).check(matches(isDisplayed()))
        onView(withText(ipName)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(withText(R.string.delete)).perform(click()) // accept dialog "are you sure to delete?"
        onView(withId(R.id.edit_text_ip)).check(doesNotExist()) // dialog should be closed
        onView(withText(ip)).check(doesNotExist())
        onView(withText(ipName)).check(doesNotExist())
    }

    private fun startFragment() {
        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)
    }
}




























































