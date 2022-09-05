package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.di.ControllerContainer
import com.myrgb.ledcontroller.network.FakeRgbRequestRepository
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import com.myrgb.ledcontroller.persistence.FakeIpAddressStorage
import com.myrgb.ledcontroller.util.DataBindingIdlingResource
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.util.monitorFragment
import org.junit.After
import org.junit.Before
import org.junit.Test

class IpAddressListFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private lateinit var fakeIpAddressStorage: FakeIpAddressStorage

    @Before
    fun setupFakeDependencies() {
        fakeIpAddressStorage = FakeIpAddressStorage()
        (getApplicationContext() as App).appContainer.controllerContainer = ControllerContainer(
            FakeRgbRequestRepository(FakeRgbRequestService(hashMapOf())), fakeIpAddressStorage
        )
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
    fun when_there_are_stored_ip_addresses_then_they_are_displayed() {
        val ip1 = "192.168.1.1"
        val ip2 = "192.168.1.2"
        val ip3 = "192.168.1.3"
        fakeIpAddressStorage.addIpAddress(ip1)
        fakeIpAddressStorage.addIpAddress(ip2)
        fakeIpAddressStorage.addIpAddress(ip3)

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withText(ip1)).check(matches(isDisplayed()))
        onView(withText(ip2)).check(matches(isDisplayed()))
        onView(withText(ip3)).check(matches(isDisplayed()))
    }

    @Test
    fun when_valid_ip_address_is_added_then_it_is_displayed_in_list() {
        val ip = "192.168.1.1"

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_add_ip)).perform(click())
        onView(withId(R.id.edit_text_ip)).perform(replaceText(ip))
        onView(withText(R.string.add)).perform(click())
        onView(withText(ip)).check(matches(isDisplayed()))
    }

    @Test
    fun when_click_on_delete_ip_button_then_dialog_asks_before_deleting_ip() {
        val ip = "192.168.1.1"
        fakeIpAddressStorage.addIpAddress(ip)

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(
            withText(
                getApplicationContext<Context>().getString(R.string.sure_to_delete_ip_address, ip)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun when_ip_address_deleted_it_is_no_longer_displayed() {
        val ip = "192.168.1.1"
        fakeIpAddressStorage.addIpAddress(ip)

        val fragmentScenario =
            launchFragmentInContainer<IpAddressListFragment>(themeResId = R.style.Theme_LedController)
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        onView(withText(ip)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_delete_ip)).perform(click())
        onView(withText(ip)).check(doesNotExist())
    }

    @Test
    fun when_invalid_ip_addresses_entered_then_an_error_message_is_displayed() {
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




























































