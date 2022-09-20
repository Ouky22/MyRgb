package com.myrgb.ledcontroller.feature.ipsettings.di

import com.myrgb.ledcontroller.feature.ipsettings.IpAddressListFragment
import dagger.Subcomponent

@Subcomponent(modules = [IpAddressModule::class])
interface IpAddressComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): IpAddressComponent
    }

    fun inject(ipAddressListFragment: IpAddressListFragment)
}