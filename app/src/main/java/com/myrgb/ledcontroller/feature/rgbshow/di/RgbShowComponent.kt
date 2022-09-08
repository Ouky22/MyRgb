package com.myrgb.ledcontroller.feature.rgbshow.di

import com.myrgb.ledcontroller.feature.rgbshow.RgbShowFragment
import dagger.Subcomponent

@Subcomponent(modules = [RgbShowModule::class])
interface RgbShowComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RgbShowComponent
    }

    fun inject(rgbShowFragment: RgbShowFragment)
}