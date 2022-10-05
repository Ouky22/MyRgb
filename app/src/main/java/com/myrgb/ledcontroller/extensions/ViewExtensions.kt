package com.myrgb.ledcontroller.extensions

import android.view.View

fun View.coordinateIsInsideView(x: Float, y: Float) =
    x > this.left && x < this.right && y > this.top && y < this.bottom