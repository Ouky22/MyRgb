package com.example.ledcontroller.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val esp32DeskBaseUrl = "http://192.168.1.249"
private const val esp32SofaBedBaseUrl = "http://192.168.1.250"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

object RgbRequestServiceDesk {
    val retrofitService: RgbRequestService by lazy {
        Retrofit.Builder()
            .baseUrl(esp32DeskBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RgbRequestService::class.java)
    }
}

object RgbRequestServiceSofaBed {
    val retrofitService: RgbRequestService by lazy {
        Retrofit.Builder()
            .baseUrl(esp32SofaBedBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RgbRequestService::class.java)
    }
}

interface RgbRequestService {
    @GET("/command/?value=$settingsCommandIdentifier.0.0.")
    suspend fun getCurrentSettings(): Response<CurrentSettingsResponse>
}
