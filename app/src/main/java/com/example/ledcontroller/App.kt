package com.example.ledcontroller

import android.app.Application
import com.example.ledcontroller.persistence.LedControllerDatabase

class App : Application() {
    val ledControllerDatabase: LedControllerDatabase by lazy {
        LedControllerDatabase.getInstance(this)
    }
}