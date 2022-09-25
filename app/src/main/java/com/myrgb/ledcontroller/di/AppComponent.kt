package com.myrgb.ledcontroller.di

import android.content.Context
import com.myrgb.ledcontroller.feature.ipsettings.addedit.di.IpAddressAddEditComponent
import com.myrgb.ledcontroller.feature.rgbcontroller.di.ControllerComponent
import com.myrgb.ledcontroller.feature.ipsettings.di.IpAddressComponent
import com.myrgb.ledcontroller.feature.rgbalarmclock.di.RgbAlarmComponent
import com.myrgb.ledcontroller.feature.rgbshow.di.RgbShowComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModuleProvide::class,
        AppModuleBind::class,
        AppSubcomponentsModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun controllerComponent(): ControllerComponent.Factory
    fun ipAddressComponent(): IpAddressComponent.Factory
    fun ipAddressAddEditComponent(): IpAddressAddEditComponent.Factory
    fun rgbShowComponent(): RgbShowComponent.Factory
    fun rgbAlarmComponent(): RgbAlarmComponent.Factory
}