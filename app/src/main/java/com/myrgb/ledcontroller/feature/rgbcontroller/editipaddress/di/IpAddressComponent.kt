package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.di

import com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress.IpAddressListFragment
import dagger.Subcomponent

@Subcomponent(modules = [IpAddressModule::class])
interface IpAddressComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): IpAddressComponent
    }

    fun inject(ipAddressListFragment: IpAddressListFragment)
}