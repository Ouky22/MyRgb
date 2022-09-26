package com.myrgb.ledcontroller

import com.myrgb.ledcontroller.di.AppComponent
import com.myrgb.ledcontroller.di.DaggerAppComponent.factory
import com.myrgb.ledcontroller.di.DaggerTestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TestApp : App() {
    override fun initializeAppComponent(): AppComponent {
        return DaggerTestAppComponent.factory().create(applicationContext)
    }
}