package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerRepository
import com.myrgb.ledcontroller.feature.rgbshow.RgbShowViewModel

class RgbShowContainer(controllerRepository: ControllerRepository) {
    val rgbShowViewModelFactory = RgbShowViewModel.Factory(controllerRepository)
}
