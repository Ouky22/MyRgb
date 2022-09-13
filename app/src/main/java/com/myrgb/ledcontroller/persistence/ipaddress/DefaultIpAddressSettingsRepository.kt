package com.myrgb.ledcontroller.persistence.ipaddress

import android.util.Log
import androidx.datastore.core.DataStore
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultIpAddressSettingsRepository @Inject constructor(
    private val ipAddressSettingsDataStore: DataStore<IpAddressSettings>
) : IpAddressSettingsRepository {

    override val ipAddressSettings: Flow<IpAddressSettings>
        get() = ipAddressSettingsDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(
                        "IpAddressSettingsRepository",
                        "Error reading IP address settings.",
                        exception
                    )
                    emit(IpAddressSettings.getDefaultInstance())
                } else
                    throw exception
            }

    override suspend fun addIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        try {
            ipAddressSettingsDataStore.updateData { currentIpAddressSettings ->
                val ipAddressAlreadyExists = currentIpAddressSettings.ipAddressNamePairsList.any {
                    it.ipAddress == ipAddressNamePair.ipAddress
                }

                if (ipAddressAlreadyExists)
                    currentIpAddressSettings
                else
                    currentIpAddressSettings.toBuilder()
                        .addIpAddressNamePairs(ipAddressNamePair)
                        .build()
            }
        } catch (exception: IOException) {
            Log.e(
                "IpAddressSettingsRepository",
                "Error adding IP address settings.",
                exception
            )
        }
    }

    override suspend fun addIpAddressNamePair(ipAddress: String, ipName: String) {
        val ipAddressNamePair = IpAddressNamePair.newBuilder()
            .setIpAddress(ipAddress)
            .setName(ipName)
            .build()
        addIpAddressNamePair(ipAddressNamePair)
    }

    override suspend fun removeIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        try {
            ipAddressSettingsDataStore.updateData { currentIpAddressSettings ->
                val indexOfIpAddressNamePair =
                    currentIpAddressSettings.ipAddressNamePairsList.indexOfFirst {
                        it.ipAddress == ipAddressNamePair.ipAddress && it.name == ipAddressNamePair.name
                    }

                val ipAddressNamePairExists = indexOfIpAddressNamePair > -1
                if (ipAddressNamePairExists)
                    currentIpAddressSettings.toBuilder()
                        .removeIpAddressNamePairs(indexOfIpAddressNamePair)
                        .build()
                else
                    currentIpAddressSettings
            }
        } catch (exception: IOException) {
            Log.e(
                "IpAddressSettingsRepository",
                "Error removing IP address settings.",
                exception
            )
        }
    }
}