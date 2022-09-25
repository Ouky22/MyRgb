package com.myrgb.ledcontroller.feature.rgbalarmclock.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RgbAlarmModule {
    @Binds
    @IntoMap
    @ViewModelKey(RgbAlarmViewModel::class)
    abstract fun bindRgbAlarmViewModel(viewModel: RgbAlarmViewModel): ViewModel
}