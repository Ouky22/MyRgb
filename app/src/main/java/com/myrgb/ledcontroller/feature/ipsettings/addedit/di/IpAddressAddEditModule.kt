package com.myrgb.ledcontroller.feature.ipsettings.addedit.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.ipsettings.addedit.IpAddressAddEditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class IpAddressAddEditModule {
    @Binds
    @IntoMap
    @ViewModelKey(IpAddressAddEditViewModel::class)
    abstract fun bindIpAddressAddEditViewModel(viewModel: IpAddressAddEditViewModel): ViewModel
}