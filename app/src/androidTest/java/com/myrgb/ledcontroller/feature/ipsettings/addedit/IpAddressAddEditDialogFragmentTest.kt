package com.myrgb.ledcontroller.feature.ipsettings.addedit

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.Companion.launch
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.TestApp
import com.myrgb.ledcontroller.di.TestAppComponent
import com.myrgb.ledcontroller.persistence.ipaddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.util.DataBindingIdlingResource
import com.myrgb.ledcontroller.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class IpAddressAddEditDialogFragmentTest {
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


    @Test
    fun when_invalid_ip_address_entered_then_an_error_message_is_displayed() {
        val invalidIp = "0.700.1.1"

        startFragment()

        onView(withId(R.id.edit_text_ip)).inRoot(isDialog()).perform(replaceText(invalidIp))
        onView(withId(R.id.btn_dialog_add_ip)).inRoot(isDialog()).perform(click())
        onView(withId(R.id.edit_text_ip)).inRoot(isDialog()).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.invalid_ip)
                )
            )
        )
    }

    @Test
    fun when_no_ip_address_entered_then_an_error_message_is_displayed() {
        startFragment()

        onView(withId(R.id.btn_dialog_add_ip)).inRoot(isDialog()).perform(click())
        onView(withId(R.id.edit_text_ip)).inRoot(isDialog()).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.no_ip_entered)
                )
            )
        )
    }

    @Test
    fun when_already_existing_ip_address_entered_then_an_error_message_is_displayed() = runTest {
        val ipAddress = "192.168.1.1"
        fakeIpAddressSettingsRepository.addIpAddressNamePair(ipAddress, "test")
        startFragment()

        onView(withId(R.id.edit_text_ip)).inRoot(isDialog()).perform(replaceText(ipAddress))
        onView(withId(R.id.btn_dialog_add_ip)).inRoot(isDialog()).perform(click())
        onView(withId(R.id.edit_text_ip)).inRoot(isDialog()).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.ip_already_exists)
                )
            )
        )
    }

    @Test
    fun when_no_ip_name_entered_then_an_error_message_is_displayed() {
        startFragment()

        onView(withId(R.id.btn_dialog_add_ip)).inRoot(isDialog()).perform(click())
        onView(withId(R.id.edit_text_name)).inRoot(isDialog()).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.no_name_entered)
                )
            )
        )
    }

    @Test
    fun when_invalid_ip_name_entered_then_an_error_message_is_displayed() {
        val invalidIpName = "  "

        startFragment()

        onView(withId(R.id.edit_text_name)).inRoot(isDialog()).perform(replaceText(invalidIpName))
        onView(withId(R.id.btn_dialog_add_ip)).inRoot(isDialog()).perform(click())
        onView(withId(R.id.edit_text_name)).inRoot(isDialog()).check(
            matches(
                hasErrorText(
                    getApplicationContext<Context>().getString(R.string.no_name_entered)
                )
            )
        )
    }

    @Test
    fun when_add_ip_address_dialog_is_canceled_then_dialog_is_closed() {
        startFragment()

        onView(withId(R.id.btn_cancel)).inRoot(isDialog()).perform(click())
        onView(withText(getApplicationContext<Context>().getString(R.string.add_rgb_controller))).check(
            doesNotExist()
        )
    }

    @Test
    fun when_editing_an_ip_address_name_pair_then_its_current_data_will_be_displayed() =
        runTest {
            val ipAddress = "192.168.1.1"
            val ipAddressName = "test"
            fakeIpAddressSettingsRepository.addIpAddressNamePair(ipAddress, ipAddressName)
            startFragment(bundleOf("ip_address" to ipAddress))

            onView(withText(R.string.edit_rgb_controller)).inRoot(isDialog())
                .check(matches(isDisplayed()))
            onView(withId(R.id.edit_text_ip)).inRoot(isDialog())
                .check(matches(withText(containsString(ipAddress))))
            onView(withId(R.id.edit_text_name)).inRoot(isDialog())
                .check(matches(withText(containsString(ipAddressName))))
        }

    private fun startFragment(fragmentArgs: Bundle = bundleOf()): FragmentScenario<IpAddressAddEditDialogFragment> {
        val fragmentScenario = launch(
            fragmentClass = IpAddressAddEditDialogFragment::class.java,
            fragmentArgs = fragmentArgs,
            themeResId = R.style.Theme_LedController
        )
        dataBindingIdlingResource.monitorFragment(fragmentScenario)

        return fragmentScenario
    }
}










































