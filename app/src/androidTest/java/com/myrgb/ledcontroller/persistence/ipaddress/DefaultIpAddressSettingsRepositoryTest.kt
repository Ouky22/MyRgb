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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    fun test_removing_an_existing_ip_address_name_pair() = runBlocking {
        val ipAddress = "192.186.1.1"
        val name = "test"
        repository.addIpAddressNamePair(ipAddress, name)
        val addedIpAddressNamePair = repository.ipAddressSettings.first().ipAddressNamePairsList[0]

        repository.removeIpAddressNamePair(addedIpAddressNamePair)

        val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
        assertTrue(currentNamePairs.isEmpty())
    }

    @Test
    fun test_removing_an_not_existing_ip_address_name_pair() = runBlocking {
        val ipAddress = "192.186.1.1"
        val name1 = "test1"
        val name2 = "test2"
        repository.addIpAddressNamePair(ipAddress, name1)

        val notExistingIpAddressNamePair =
            IpAddressNamePair.newBuilder().setName(name2).setIpAddress(ipAddress).build()
        repository.removeIpAddressNamePair(notExistingIpAddressNamePair)

        val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
        assertEquals(1, currentNamePairs.size)
        assertTrue(currentNamePairs.any { it.ipAddress == ipAddress && it.name == name1 })
    }

    @Test

    fun test_removing_ip_address_name_pair_when_there_are_no_ip_address_name_pairs() =
        runBlocking {
            val ipAddress = "192.186.1.1"
            val name = "test1"

            val notExistingIpAddressNamePair =
                IpAddressNamePair.newBuilder().setName(name).setIpAddress(ipAddress).build()
            repository.removeIpAddressNamePair(notExistingIpAddressNamePair)

            val currentNamePairs = repository.ipAddressSettings.first().ipAddressNamePairsList
            assertTrue(currentNamePairs.isEmpty())
        }
}























































