package com.myrgb.ledcontroller.feature.rgbalarmclock.di

import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListFragment
import dagger.Subcomponent

@Subcomponent(modules = [RgbAlarmModule::class])
interface RgbAlarmComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RgbAlarmComponent
    }

    fun inject(rgbAlarmListFragment: RgbAlarmListFragment)
}