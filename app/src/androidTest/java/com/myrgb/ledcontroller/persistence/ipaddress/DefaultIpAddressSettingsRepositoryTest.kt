package com.myrgb.ledcontroller.persistence.ipaddress

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATA_STORE_FILE_NAME = "test_data_store.pb"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DefaultIpAddressSettingsRepositoryTest {
    private val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher + Job())

    private val testDataStore: DataStore<IpAddressSettings> = DataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = { testContext.dataStoreFile(TEST_DATA_STORE_FILE_NAME) },
        serializer = IpAddressSettingsSerializer
    )

    private val repository = DefaultIpAddressSettingsRepository(testDataStore)

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testCoroutineScope.runBlockingTest {
            testDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
        testCoroutineScope.cancel()
    }

    @Test
    fun when_getting_ip_address_name_pair_by_id_and_it_exists_then_it_is_returned() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.168.1.100"
            val name = "test"
            repository.addIpAddressNamePair(ipAddress, name)

            val ipAddressNamePair = repository.getIpAddressNamePairByIpAddress(ipAddress)

            assertNotNull(ipAddressNamePair)
            assertEquals(ipAddress, ipAddressNamePair!!.ipAddress)
            assertEquals(name, ipAddressNamePair.name)
        }

    @Test
    fun when_getting_ip_address_name_pair_by_id_and_it_not_exists_then_null_is_returned() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.168.1.100"
            val name = "test"
            repository.addIpAddressNamePair(ipAddress, name)

            assertNull(repository.getIpAddressNamePairByIpAddress("192.168.1.1"))
        }

    @Test
    fun when_getting_ip_address_name_pair_by_id_and_no_ip_settings_exist_then_null_is_returned() =
        testCoroutineScope.runBlockingTest {
            assertNull(repository.getIpAddressNamePairByIpAddress("192.168.1.1"))
        }

    @Test
    fun test_adding_an_ip_address_name_pair() = testCoroutineScope.runBlockingTest {
        val ipAddress = "192.186.1.1"
        val name = "test"

        repository.addIpAddressNamePair(ipAddress, name)

        val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
        assertEquals(1, currentNamePairs.size)
        assertTrue(currentNamePairs.any { it.ipAddress == ipAddress && it.name == name })
    }

    @Test
    fun when_adding_ip_address_name_pair_with_already_existing_ip_address_then_it_is_not_added() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.186.1.1"
            val name1 = "test1"
            val name2 = "test2"
            repository.addIpAddressNamePair(ipAddress, name1)

            repository.addIpAddressNamePair(ipAddress, name2)

            val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
            assertEquals(1, currentNamePairs.size)
            assertTrue(currentNamePairs.any { it.ipAddress == ipAddress && it.name == name1 })
        }

    @Test
    fun when_updating_an_existing_ip_address_name_pair_then_it_will_be_updated() =
        testCoroutineScope.runBlockingTest {
            val oldIpAddress = "192.186.1.1"
            val oldName = "old test name"
            repository.addIpAddressNamePair(oldIpAddress, oldName)

            val newIpAddress = "192.168.1.2"
            val newName = "new test name"
            repository.updateIpAddressNamePair(
                oldIpAddress = oldIpAddress,
                newIpAddress = newIpAddress,
                newIpName = newName
            )

            val currentIpAddressNamePairs =
                repository.ipAddressSettings.first().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertFalse(currentIpAddressNamePairs.any {
                it.ipAddress == oldIpAddress && it.name == oldName
            })
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == newIpAddress && it.name == newName
            })
        }

    @Test
    fun when_updating_a_not_existing_ip_address_name_pair_then_nothing_will_change() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.186.1.1"
            val name = "test name"
            repository.addIpAddressNamePair(ipAddress, name)

            val notExistingIpAddress = "192.168.1.3"
            repository.updateIpAddressNamePair(
                oldIpAddress = notExistingIpAddress,
                newIpAddress = notExistingIpAddress,
                newIpName = "name"
            )

            val currentIpAddressNamePairs =
                repository.ipAddressSettings.first().ipAddressNamePairsList
            assertEquals(1, currentIpAddressNamePairs.size)
            assertFalse(currentIpAddressNamePairs.any {
                it.ipAddress == notExistingIpAddress
            })
            assertTrue(currentIpAddressNamePairs.any {
                it.ipAddress == ipAddress && it.name == name
            })
        }

    @Test
    fun test_removing_an_existing_ip_address_name_pair() = testCoroutineScope.runBlockingTest {
        val ipAddress = "192.186.1.1"
        val name = "test"
        repository.addIpAddressNamePair(ipAddress, name)
        val addedIpAddressNamePair = repository.ipAddressSettings.first().ipAddressNamePairsList[0]

        repository.removeIpAddressNamePair(addedIpAddressNamePair.ipAddress)

        val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
        assertTrue(currentNamePairs.isEmpty())
    }

    @Test
    fun test_removing_an_not_existing_ip_address_name_pair() = testCoroutineScope.runBlockingTest {
        val ipAddress1 = "192.186.1.1"
        val ipAddress2 = "192.186.1.2"
        val name = "test"
        repository.addIpAddressNamePair(ipAddress1, name)

        repository.removeIpAddressNamePair(ipAddress2)

        val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
        assertEquals(1, currentNamePairs.size)
        assertTrue(currentNamePairs.any { it.ipAddress == ipAddress1 && it.name == name })
    }

    @Test
    fun test_removing_ip_address_name_pair_when_there_are_no_ip_address_name_pairs() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.186.1.1"
            val name = "test1"

            val notExistingIpAddressNamePair =
                IpAddressNamePair.newBuilder().setName(name).setIpAddress(ipAddress).build()
            repository.removeIpAddressNamePair(notExistingIpAddressNamePair.ipAddress)

            val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
            assertTrue(currentNamePairs.isEmpty())
        }

    @Test
    fun when_ip_address_exists_then_existsIpAddressNamePairWithIpAddress_returns_true() =
        testCoroutineScope.runBlockingTest {
            val ipAddress = "192.186.1.1"
            repository.addIpAddressNamePair(ipAddress, "test")

            assertTrue(repository.existsIpAddressNamePairWithIpAddress(ipAddress))
        }

    @Test
    fun when_ip_address_not_exists_then_existsIpAddressNamePairWithIpAddress_returns_false() =
        testCoroutineScope.runBlockingTest {
            repository.addIpAddressNamePair("192.168.1.1", "test")

            assertFalse(repository.existsIpAddressNamePairWithIpAddress("192.168.1.2"))
        }

    @Test
    fun when_there_are_no_ip_settings_then_existsIpAddressNamePairWithIpAddress_returns_false() =
        testCoroutineScope.runBlockingTest {
            assertFalse(repository.existsIpAddressNamePairWithIpAddress("192.168.1.2"))
        }
}























































