package com.myrgb.ledcontroller.feature.ipsettings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.testutil.MainDispatcherRule
import com.myrgb.ledcontroller.persistence.ipAddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


@ExperimentalCoroutinesApi
class IpAddressListViewModelTest {
    private lateinit var ipAddressListViewModel: IpAddressListViewModel
    private lateinit var fakeIpAddressSettingsRepository: IpAddressSettingsRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        fakeIpAddressSettingsRepository = FakeIpAddressSettingsRepository()
        ipAddressListViewModel = IpAddressListViewModel(fakeIpAddressSettingsRepository)
    }

    @Test
    fun `when an ip address name pair is removed then it is gone`() = runTest {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "test"
        fakeIpAddressSettingsRepository.addIpAddressNamePair(ipAddress, ipAddressName)
        val addedIpAddressNamePair = ipAddressListViewModel.ipAddressNamePairs.value[0]

        ipAddressListViewModel.removeIpAddressNamePair(addedIpAddressNamePair)

        assertTrue(ipAddressListViewModel.ipAddressNamePairs.value.isEmpty())
    }
}














































