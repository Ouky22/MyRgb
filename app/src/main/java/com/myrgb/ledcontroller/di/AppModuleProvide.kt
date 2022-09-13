package com.myrgb.ledcontroller.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.network.RgbRequestService
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsSerializer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


const val IP_ADDRESS_SETTINGS_DATA_STORE_FILE_NAME = "ip_address_settings.pb"

@Module
class AppModuleProvide {
    @Singleton
    @Provides
    fun provideRgbRequestService(moshi: Moshi): RgbRequestService {
        return Retrofit.Builder()
            .baseUrl("http://localhost/") // localhost is used because retrofit forces baseUrl, but it will be overwritten by the specified methods in RgbRequestService
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RgbRequestService::class.java)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideIpAddressSettingsDataStore(context: Context): DataStore<IpAddressSettings> {
        return DataStoreFactory.create(
            serializer = IpAddressSettingsSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { IpAddressSettings.getDefaultInstance() }
            ),
            produceFile = { context.dataStoreFile(IP_ADDRESS_SETTINGS_DATA_STORE_FILE_NAME) }
        )
    }
}