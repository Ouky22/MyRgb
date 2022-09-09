package com.myrgb.ledcontroller.feature.editipaddress.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.editipaddress.IpAddressViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class IpAddressModule {
    @Binds
    @IntoMap
    @ViewModelKey(IpAddressViewModel::class)
    abstract fun bindIpAddressViewModel(viewModel: IpAddressViewModel): ViewModel
}