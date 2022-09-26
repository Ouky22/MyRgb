package com.myrgb.ledcontroller.feature.rgbalarmclock.list.di

import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListFragment
import dagger.Subcomponent

@Subcomponent(modules = [RgbAlarmModule::class])
interface RgbAlarmListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RgbAlarmListComponent
    }

    fun inject(rgbAlarmListFragment: RgbAlarmListFragment)
}