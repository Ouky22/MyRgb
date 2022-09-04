package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel
import com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.IpAddressViewModel
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.persistence.IpAddressStorage


class ControllerContainer(
    rgbRequestRepository: RgbRequestRepository,
    ipAddressStorage: IpAddressStorage
) {
    val controllerViewModelFactory =
        ControllerViewModel.Factory(rgbRequestRepository, ipAddressStorage)

    val ipAddressViewModelFactory =
        IpAddressViewModel.Factory(ipAddressStorage)
}