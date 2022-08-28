package com.myrgb.ledcontroller.util

import android.view.InputDevice
import android.view.MotionEvent
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap

fun relativeClickAt(x: Float, y: Float) = GeneralClickAction(
    Tap.SINGLE,
    { view ->
        val positionOfView = IntArray(2)
        view.getLocationOnScreen(positionOfView)

        val absoluteX = positionOfView[0] + x
        val absoluteY = positionOfView[1] + y

        floatArrayOf(absoluteX, absoluteY)
    },
    Press.FINGER,
    InputDevice.SOURCE_TOUCHSCREEN,
    MotionEvent.ACTION_DOWN
)