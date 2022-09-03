package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbshow.RgbShowViewModel
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.persistence.IpAddressStorage

class RgbShowContainer(
    rgbRequestRepository: RgbRequestRepository,
    ipAddressStorage: IpAddressStorage
) {
    val rgbShowViewModelFactory = RgbShowViewModel.Factory(rgbRequestRepository, ipAddressStorage)
}
