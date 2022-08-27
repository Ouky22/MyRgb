package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.DefaultControllerRepository
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel


class ControllerContainer(defaultControllerRepository: DefaultControllerRepository) {
    val controllerViewModelFactory = ControllerViewModel.Factory(defaultControllerRepository)
}