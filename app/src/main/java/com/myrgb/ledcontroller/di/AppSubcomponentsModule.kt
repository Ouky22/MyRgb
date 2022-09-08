package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.di.ControllerComponent
import com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.di.IpAddressComponent
import com.myrgb.ledcontroller.feature.rgbshow.di.RgbShowComponent
import dagger.Module

@Module(
    subcomponents = [
        ControllerComponent::class,
        IpAddressComponent::class,
        RgbShowComponent::class
    ]
)
class AppSubcomponentsModule