package com.myrgb.ledcontroller.feature.rgbcontroller

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.myrgb.ledcontroller.R

@BindingAdapter("status_border")
fun statusBorder(button: com.google.android.material.button.MaterialButton, isActivated: Boolean) {
    if (isActivated)
        button.setStrokeColorResource(R.color.btn_color_on)
    else
        button.setStrokeColorResource(R.color.btn_color_off)
}

@BindingAdapter("start_stop_icon")
fun startStopIcon(imageView: ImageView, started: Boolean) {
    if (started)
        imageView.setImageResource(R.drawable.ic_stop)
    else
        imageView.setImageResource(R.drawable.ic_start_rgb_show)
}
