package com.myrgb.ledcontroller.feature.rgbcontroller.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ControllerModule {
    @Binds
    @IntoMap
    @ViewModelKey(ControllerViewModel::class)
    abstract fun bindControllerViewModel(viewModel: ControllerViewModel): ViewModel
}