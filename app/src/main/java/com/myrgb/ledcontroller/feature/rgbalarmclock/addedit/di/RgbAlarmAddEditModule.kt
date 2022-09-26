package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.di

import androidx.lifecycle.ViewModel
import com.myrgb.ledcontroller.di.ViewModelKey
import com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.RgbAlarmAddEditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RgbAlarmAddEditModule {
    @Binds
    @IntoMap
    @ViewModelKey(RgbAlarmAddEditViewModel::class)
    abstract fun bindRgbAlarmAddEditViewModel(viewModel: RgbAlarmAddEditViewModel): ViewModel
}