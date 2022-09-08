package com.myrgb.ledcontroller

import com.myrgb.ledcontroller.di.AppComponent
import com.myrgb.ledcontroller.di.DaggerTestAppComponent

class TestApp : App() {
    override fun initializeAppComponent(): AppComponent {
        return DaggerTestAppComponent.create()
    }
}