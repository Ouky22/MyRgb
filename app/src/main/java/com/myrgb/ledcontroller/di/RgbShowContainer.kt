package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.DefaultControllerRepository
import com.myrgb.ledcontroller.feature.rgbshow.RgbShowViewModel

class RgbShowContainer(defaultControllerRepository: DefaultControllerRepository) {
    val rgbShowViewModelFactory = RgbShowViewModel.Factory(defaultControllerRepository)
}
