package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.network.RgbRequestService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

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
}