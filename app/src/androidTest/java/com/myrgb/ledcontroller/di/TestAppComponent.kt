package com.myrgb.ledcontroller.di

import android.content.Context
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragmentTest
import com.myrgb.ledcontroller.feature.ipsettings.IpAddressListFragmentTest
import com.myrgb.ledcontroller.feature.ipsettings.addedit.IpAddressAddEditDialogFragmentTest
import com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.RgbAlarmAddEditFragmentTest
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListFragmentTest
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModuleBind::class,
        TestAppModuleProvide::class,
        AppSubcomponentsModule::class,
        ViewModelModule::class
    ]
)
@ExperimentalCoroutinesApi
interface TestAppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestAppComponent
    }

    fun inject(testFragment: ControllerFragmentTest)

    fun inject(testFragment: IpAddressListFragmentTest)

    fun inject(testFragment: IpAddressAddEditDialogFragmentTest)

    fun inject(testFragment: RgbAlarmListFragmentTest)

    fun inject(testFragment: RgbAlarmAddEditFragmentTest)
}