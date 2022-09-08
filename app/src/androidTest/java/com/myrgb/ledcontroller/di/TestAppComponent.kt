package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragmentTest
import com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.IpAddressListFragmentTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModule::class,
        AppSubcomponentsModule::class,
        ViewModelModule::class
    ]
)
interface TestAppComponent : AppComponent {
    @ExperimentalCoroutinesApi
    fun inject(testFragment: ControllerFragmentTest)

    fun inject(testFragment: IpAddressListFragmentTest)
}