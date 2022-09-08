package com.myrgb.ledcontroller.feature.rgbshow.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.rgbshow.RgbShowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RgbShowModule {
    @Binds
    @IntoMap
    @ViewModelKey(RgbShowViewModel::class)
    abstract fun bindRgbShowViewModel(viewModel: RgbShowViewModel): ViewModel
}