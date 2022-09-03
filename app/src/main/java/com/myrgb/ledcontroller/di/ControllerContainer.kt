package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel
import com.myrgb.ledcontroller.network.RgbRequestRepository


class ControllerContainer(rgbRequestRepository: RgbRequestRepository) {
    val controllerViewModelFactory = ControllerViewModel.Factory(rgbRequestRepository)
}