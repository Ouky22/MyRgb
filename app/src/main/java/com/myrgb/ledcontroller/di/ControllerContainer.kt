package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerRepository
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel


class ControllerContainer(defaultControllerRepository: ControllerRepository) {
    val controllerViewModelFactory = ControllerViewModel.Factory(defaultControllerRepository)
}