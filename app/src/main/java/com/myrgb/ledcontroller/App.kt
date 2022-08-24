package com.myrgb.ledcontroller

import android.app.Application
import com.myrgb.ledcontroller.persistence.LedControllerDatabase

class App : Application() {
    val ledControllerDatabase: LedControllerDatabase by lazy {
        LedControllerDatabase.getInstance(this)
    }
}