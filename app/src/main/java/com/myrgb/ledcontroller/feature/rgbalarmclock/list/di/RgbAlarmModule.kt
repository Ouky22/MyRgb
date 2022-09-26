package com.myrgb.ledcontroller.feature.rgbalarmclock.list.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RgbAlarmModule {
    @Binds
    @IntoMap
    @ViewModelKey(RgbAlarmListViewModel::class)
    abstract fun bindRgbAlarmListViewModel(viewModel: RgbAlarmListViewModel): ViewModel
}