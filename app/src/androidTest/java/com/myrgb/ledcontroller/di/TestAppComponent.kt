package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragmentTest
import com.myrgb.ledcontroller.feature.ipsettings.IpAddressListFragmentTest
import com.myrgb.ledcontroller.feature.ipsettings.addedit.IpAddressAddEditDialogFragmentTest
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
@ExperimentalCoroutinesApi
interface TestAppComponent : AppComponent {
    fun inject(testFragment: ControllerFragmentTest)

    fun inject(testFragment: IpAddressListFragmentTest)

    fun inject(testFragment: IpAddressAddEditDialogFragmentTest)
}