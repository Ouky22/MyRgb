package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.di

import com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.RgbAlarmAddEditFragment
import dagger.Subcomponent

@Subcomponent(modules = [RgbAlarmAddEditModule::class])
interface RgbAlarmAddEditComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): RgbAlarmAddEditComponent
    }

    fun inject(rgbAlarmAddEditFragment: RgbAlarmAddEditFragment)
}