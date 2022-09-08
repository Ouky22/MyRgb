package com.myrgb.ledcontroller.feature.rgbcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragment
import dagger.Subcomponent

@Subcomponent(modules = [ControllerModule::class])
interface ControllerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ControllerComponent
    }

    fun inject(controllerFragment: ControllerFragment)
}