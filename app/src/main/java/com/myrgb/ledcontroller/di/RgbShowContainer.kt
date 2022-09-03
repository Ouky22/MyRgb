package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbshow.RgbShowViewModel
import com.myrgb.ledcontroller.network.RgbRequestRepository

class RgbShowContainer(rgbRequestRepository: RgbRequestRepository) {
    val rgbShowViewModelFactory = RgbShowViewModel.Factory(rgbRequestRepository)
}
