package com.myrgb.ledcontroller.feature.editipaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.testutil.MainDispatcherRule
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.persistence.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


@ExperimentalCoroutinesApi
class IpAddressViewModelTest {
    private lateinit var ipAddressViewModel: IpAddressViewModel
    private lateinit var fakeIpAddressSettingsRepository: IpAddressSettingsRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        fakeIpAddressSettingsRepository = FakeIpAddressSettingsRepository()
        ipAddressViewModel = IpAddressViewModel(fakeIpAddressSettingsRepository)
    }

    @Test
    fun `when ip address name and ip address are valid then an ip address name pair will be added`() {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "test"

        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)

        assertEquals(1, ipAddressViewModel.ipAddressNamePairs.value.size)
        assertTrue(ipAddressViewModel.ipAddressNamePairs.value.any {
            it.name == ipAddressName && it.ipAddress == ipAddress
        })
    }

    @Test
    fun `when ip address is invalid then no ip address name pair will be added`() {
        val ipAddress = "192.1686.1.1"
        val ipAddressName = "test"

        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)

        assertTrue(ipAddressViewModel.ipAddressNamePairs.value.isEmpty())
    }

    @Test
    fun `when ip address name is invalid then no ip address name pair will be added`() {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "  "

        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)

        assertTrue(ipAddressViewModel.ipAddressNamePairs.value.isEmpty())
    }

    @Test
    fun `when ip address name and ip address are invalid then no ip address name pair will be added`() {
        val ipAddress = "1923.168.1.1"
        val ipAddressName = ""

        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)

        assertTrue(ipAddressViewModel.ipAddressNamePairs.value.isEmpty())
    }

    @Test
    fun `when an ip address name pair is removed then it is gone`() {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "test"
        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)
        val addedIpAddressNamePair = ipAddressViewModel.ipAddressNamePairs.value[0]

        ipAddressViewModel.removeIpAddressNamePair(addedIpAddressNamePair)

        assertTrue(ipAddressViewModel.ipAddressNamePairs.value.isEmpty())
    }

    @Test
    fun `when ip address input is valid then validation is successful`() {
        val ipAddressInput = "192.168.1.1"

        val validationResult = ipAddressViewModel.validateIpAddressTextInput(ipAddressInput)

        assertTrue(validationResult is Success)
    }

    @Test
    fun `when ip address input is empty string then validation is not successful`() {
        val ipAddressInput = ""

        val validationResult = ipAddressViewModel.validateIpAddressTextInput(ipAddressInput)

        assertTrue(validationResult is Failure)
        assertEquals(R.string.no_ip_entered, (validationResult as Failure).messageResourceId)
    }

    @Test
    fun `when ip address input is null then validation is not successful`() {
        val ipAddressInput = null

        val validationResult = ipAddressViewModel.validateIpAddressTextInput(ipAddressInput)

        assertTrue(validationResult is Failure)
        assertEquals(R.string.no_ip_entered, (validationResult as Failure).messageResourceId)
    }

    @Test
    fun `when ip address structure is not valid then validation is not successful`() {
        val ipAddressInput = "192.1111.."

        val validationResult = ipAddressViewModel.validateIpAddressTextInput(ipAddressInput)

        assertTrue(validationResult is Failure)
        assertEquals(R.string.invalid_ip, (validationResult as Failure).messageResourceId)

    }

    @Test
    fun `when ip address already exists then validation is not successful`() = runTest {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "test"
        ipAddressViewModel.addIpAddressNamePair(ipAddress, ipAddressName)

        val validationResult = ipAddressViewModel.validateIpAddressTextInput(ipAddress)

        assertTrue(validationResult is Failure)
        assertEquals(R.string.ip_already_exists, (validationResult as Failure).messageResourceId)
    }

    @Test
    fun `when ip name is valid then validation is successful`() {
        val ipName = "test"

        val validationResult = ipAddressViewModel.validateIpAddressNameTextInput(ipName)

        assertTrue(validationResult is Success)
    }

    @Test
    fun `when ip name is empty string then validation is not successful`() {
        val ipName = ""

        val validationResult = ipAddressViewModel.validateIpAddressNameTextInput(ipName)

        assertTrue(validationResult is Failure)
    }

    @Test
    fun `when ip name is null then validation is not successful`() {
        val ipName = null

        val validationResult = ipAddressViewModel.validateIpAddressNameTextInput(ipName)

        assertTrue(validationResult is Failure)
    }
}














































