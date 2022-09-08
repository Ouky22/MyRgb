package com.myrgb.ledcontroller

import android.app.Application
import androidx.fragment.app.Fragment
import com.myrgb.ledcontroller.di.AppContainer
import com.myrgb.ledcontroller.di.AppComponent
import com.myrgb.ledcontroller.di.DaggerAppComponent
import com.myrgb.ledcontroller.persistence.LedControllerDatabase

open class App : Application() {
    val ledControllerDatabase: LedControllerDatabase by lazy {
        LedControllerDatabase.getInstance(this)
    }

    val appComponent: AppComponent by lazy {
        initializeAppComponent()
    }

    open fun initializeAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    val appContainer: AppContainer by lazy { AppContainer(this) }
}