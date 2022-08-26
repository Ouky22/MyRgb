package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerRepository
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel


class ControllerContainer(controllerRepository: ControllerRepository) {
    val controllerViewModelFactory = ControllerViewModel.Factory(controllerRepository)
}