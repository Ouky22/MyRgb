package com.myrgb.ledcontroller

import android.app.Application
import com.myrgb.ledcontroller.di.AppComponent
import com.myrgb.ledcontroller.di.DaggerAppComponent

open class App : Application() {
    val appComponent: AppComponent by lazy {
        initializeAppComponent()
    }

    open fun initializeAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}