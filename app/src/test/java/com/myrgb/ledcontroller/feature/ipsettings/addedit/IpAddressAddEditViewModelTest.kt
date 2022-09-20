package com.myrgb.ledcontroller.feature.ipsettings.addedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.persistence.ipAddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class IpAddressAddEditViewModelTest {
    private lateinit var ipAddressAddEditViewModel: IpAddressAddEditViewModel
    private lateinit var ipAddressSettingsRepository: FakeIpAddressSettingsRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        ipAddressSettingsRepository = FakeIpAddressSettingsRepository()
        ipAddressAddEditViewModel = IpAddressAddEditViewModel(ipAddressSettingsRepository)
    }


    @Test
    fun `when ip address name and ip address are valid then an ip address name pair will be added`() =
        runTest {
            val ipAddress = "192.168.1.1"
            val ipAddressName = "test"
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }

            ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(ipAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.name == ipAddressName && it.ipAddress == ipAddress
            })

            collectJob.cancel()
        }

    @Test
    fun `when ip address has invalid structure then no ip address name pair will be added`() =
        runTest {
            val ipAddress = "192.1686.1.1"
            val ipAddressName = "test"
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }

            ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(ipAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            assertTrue(emittedIpAddressSettings.isEmpty())

            collectJob.cancel()
        }

    @Test
    fun `when ip address name is invalid then no ip address name pair will be added`() = runTest {
        val ipAddress = "192.168.1.1"
        val ipAddressName = "  "
        val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
        }

        ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress)
        ipAddressAddEditViewModel.updateIpAddressNameTextInput(ipAddressName)
        ipAddressAddEditViewModel.submitIpAddressSettingsData()

        assertTrue(emittedIpAddressSettings.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun `when ip address name and ip address are invalid then no ip address name pair will be added`() =
        runTest {
            val ipAddress = "1923.168.1.1"
            val ipAddressName = ""
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }

            ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(ipAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            assertTrue(emittedIpAddressSettings.isEmpty())

            collectJob.cancel()
        }

    @Test
    fun `when ip address already exists then no ip address name pair will be added`() = runTest {
        val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
        }
        val ipAddress = "192.168.1.1"
        val ipAddressName = "test"
        ipAddressSettingsRepository.addIpAddressNamePair(ipAddress, ipAddressName)

        ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress)
        ipAddressAddEditViewModel.updateIpAddressNameTextInput("name")
        ipAddressAddEditViewModel.submitIpAddressSettingsData()

        val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
        assertEquals(1, currentIpAddressNamePairs.size)
        assertTrue(currentIpAddressNamePairs.any {
            it.name == ipAddressName && it.ipAddress == ipAddress
        })

        collectJob.cancel()
    }

    @Test
    fun `when ip address name and ip address are changed and valid then corresponding ip address name pair will be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)

            val newIpAddress = "192.168.1.2"
            val newIpAddressName = "new name"
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressTextInput(newIpAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(newIpAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.name == newIpAddressName && it.ipAddress == newIpAddress
            })

            collectJob.cancel()
        }

    @Test
    fun `when ip address is changed and valid then corresponding ip address name pair will be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)


            val newIpAddress = "192.168.1.2"
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressTextInput(newIpAddress)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.name == oldIpAddressName && it.ipAddress == newIpAddress
            })

            collectJob.cancel()
        }

    @Test
    fun `when ip address name is changed and valid then corresponding ip address name pair will be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)

            val newIpAddressName = "new name"
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(newIpAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.name == newIpAddressName && it.ipAddress == oldIpAddress
            })

            collectJob.cancel()
        }

    @Test
    fun `when ip address is changed and has invalid structure then corresponding ip address name pair won't be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)

            val newIpAddress = "192.1687.1.2"
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressTextInput(newIpAddress)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == oldIpAddress && it.name == oldIpAddressName
            })

            collectJob.cancel()
        }

    @Test
    fun `when ip address name is changed and invalid then corresponding ip address name won't be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)

            val newIpAddressName = ""
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(newIpAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == oldIpAddress && it.name == oldIpAddressName
            })

            collectJob.cancel()
        }

    @Test
    fun `when updating ip address name and ip address and both are invalid then corresponding ip address name pair won't be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val oldIpAddress = "192.168.1.1"
            val oldIpAddressName = "test"
            ipAddressSettingsRepository.addIpAddressNamePair(oldIpAddress, oldIpAddressName)

            val newIpAddress = "192.1"
            val newIpAddressName = ""
            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(oldIpAddress)
            ipAddressAddEditViewModel.updateIpAddressTextInput(newIpAddress)
            ipAddressAddEditViewModel.updateIpAddressNameTextInput(newIpAddressName)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == oldIpAddress && it.name == oldIpAddressName
            })

            collectJob.cancel()
        }

    @Test
    fun `when updating ip address to ip address that already exists then corresponding ip address name pair won't be updated`() =
        runTest {
            val emittedIpAddressSettings = mutableListOf<IpAddressSettings>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                ipAddressSettingsRepository.ipAddressSettings.toList(emittedIpAddressSettings)
            }
            val ipAddress1 = "192.168.1.1"
            val ipAddressName1 = "name1"
            val ipAddress2 = "192.168.1.2"
            val ipAddressName2 = "name2"
            ipAddressSettingsRepository.addIpAddressNamePair(ipAddress1, ipAddressName1)
            ipAddressSettingsRepository.addIpAddressNamePair(ipAddress2, ipAddressName2)

            ipAddressAddEditViewModel.setSelectedIpAddressNamePair(ipAddress1)
            ipAddressAddEditViewModel.updateIpAddressTextInput(ipAddress2)
            ipAddressAddEditViewModel.submitIpAddressSettingsData()

            val currentIpAddressNamePairs = emittedIpAddressSettings.last().ipAddressNamePairsList
            assertEquals(2, currentIpAddressNamePairs.size)
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == ipAddress1 && it.name == ipAddressName1
            })
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == ipAddress2 && it.name == ipAddressName2
            })

            collectJob.cancel()
        }
}



















































