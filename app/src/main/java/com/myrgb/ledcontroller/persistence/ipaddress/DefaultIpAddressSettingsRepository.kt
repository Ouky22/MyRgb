package com.myrgb.ledcontroller.persistence.ipaddress

import android.util.Log
import androidx.datastore.core.DataStore
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import kotlinx.coroutines.flow.*
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

    override suspend fun getIpAddressNamePairByIpAddress(ipAddress: String) =
        ipAddressSettings.firstOrNull()?.ipAddressNamePairsList?.firstOrNull { it.ipAddress == ipAddress }

    /**
     * This method ensures that an ipAddressNamePair whose ip address already exists is not added
     */
    override suspend fun addIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        try {
            ipAddressSettingsDataStore.updateData { currentIpAddressSettings ->
                if (existsIpAddressNamePairWithIpAddress(ipAddressNamePair.ipAddress))
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

    override suspend fun updateIpAddressNamePair(
        oldIpAddress: String, newIpAddress: String, newIpName: String
    ) {
        if (existsIpAddressNamePairWithIpAddress(oldIpAddress)) {
            removeIpAddressNamePair(oldIpAddress)
            addIpAddressNamePair(newIpAddress, newIpName)
        }
    }

    override suspend fun removeIpAddressNamePair(ipAddress: String) {
        try {
            ipAddressSettingsDataStore.updateData { currentIpAddressSettings ->
                val indexOfIpAddressNamePair =
                    currentIpAddressSettings.ipAddressNamePairsList.indexOfFirst {
                        it.ipAddress == ipAddress
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

    override suspend fun existsIpAddressNamePairWithIpAddress(ipAddress: String): Boolean {
        return ipAddressSettings.firstOrNull()?.ipAddressNamePairsList?.any {
            it.ipAddress == ipAddress
        } ?: false
    }
}


















































