package com.myrgb.ledcontroller.feature.ipsettings.addedit.di

import com.myrgb.ledcontroller.feature.ipsettings.addedit.IpAddressAddEditDialogFragment
import dagger.Subcomponent

@Subcomponent(modules = [IpAddressAddEditModule::class])
interface IpAddressAddEditComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): IpAddressAddEditComponent
    }

    fun inject(ipAddressAddEditDialogFragment: IpAddressAddEditDialogFragment)
}