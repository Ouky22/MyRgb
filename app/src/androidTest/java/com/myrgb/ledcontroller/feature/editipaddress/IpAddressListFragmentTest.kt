package com.myrgb.ledcontroller.feature.editipaddress

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.persistence.ipaddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.util.DataBindingIdlingResource
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.util.monitorFragment
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

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
        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

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
    fun when_valid_ip_address_and_name_is_added_then_it_is_displayed_in_list() {
        val ip = "192.168.1.1"
        val name = "test"

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_add_ip)).perform(click())
        onView(withId(R.id.edit_text_name)).perform(replaceText(name))
        onView(withId(R.id.edit_text_ip)).perform(replaceText(ip))
        onView(withText(R.string.add)).perform(click())
        onView(withId(R.id.edit_text_name)).check(doesNotExist())
        onView(withId(R.id.edit_text_ip)).check(doesNotExist())
        onView(withText(name)).check(matches(isDisplayed()))
        onView(withText(ip)).check(matches(isDisplayed()))
    }

    @Test
    fun when_click_on_delete_ip_button_then_dialog_asks_before_deleting_ip() = runBlocking<Unit> {
        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        val ip = "192.168.1.1"
        val ipName = "test"
        fakeIpAddressSettingsRepository.emit(createIpAddressSettings(listOf(ip), ipName))

        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(
            withText(
                getApplicationContext<Context>().getString(
                    R.string.sure_to_delete_ip_address,
                    "\"$ipName\" ($ip)"
                )
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun when_ip_address_deleted_then_it_is_no_longer_displayed() = runBlocking<Unit> {
        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        val ip = "192.168.1.1"
        val ipName = "test"
        fakeIpAddressSettingsRepository.emit(createIpAddressSettings(listOf(ip), ipName))

        onView(withText(ip)).check(matches(isDisplayed()))
        onView(withText(ipName)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(withText(ip)).check(doesNotExist())
        onView(withText(ipName)).check(doesNotExist())
    }

    @Test
    fun when_invalid_ip_address_entered_then_an_error_message_is_displayed() {
        val invalidIp = "0.700.1.1"

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_add_ip)).perform(click())
        onView(withId(R.id.edit_text_ip)).perform(replaceText(invalidIp))
        onView(withId(R.id.btn_add_ip_dialog)).perform(click())
        onView(withId(R.id.edit_text_ip)).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.invalid_ip)
                )
            )
        )
    }

    @Test
    fun when_invalid_ip_name_entered_then_an_erro_message_is_displayed() {
        val invalidIpName = "  "

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_add_ip)).perform(click())
        onView(withId(R.id.edit_text_name)).perform(replaceText(invalidIpName))
        onView(withId(R.id.btn_add_ip_dialog)).perform(click())
        onView(withId(R.id.edit_text_name)).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.no_name_entered)
                )
            )
        )
    }

    @Test
    fun when_add_ip_address_dialog_is_canceled_then_dialog_is_closed() {
        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_add_ip)).perform(click())
        onView(withId(R.id.btn_cancel)).perform(click())
        onView(
            withText(
                getApplicationContext<Context>().getString(R.string.add_rgb_controller)
            )
        ).check(doesNotExist())
        onView(withId(R.id.rv_ip_addresses)).check(matches(isCompletelyDisplayed()))
    }
}




























































