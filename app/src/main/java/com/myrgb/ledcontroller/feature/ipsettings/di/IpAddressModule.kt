package com.myrgb.ledcontroller.feature.ipsettings.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.ipsettings.IpAddressListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class IpAddressModule {
    @Binds
    @IntoMap
    @ViewModelKey(IpAddressListViewModel::class)
    abstract fun bindIpAddressViewModel(viewModel: IpAddressListViewModel): ViewModel
}