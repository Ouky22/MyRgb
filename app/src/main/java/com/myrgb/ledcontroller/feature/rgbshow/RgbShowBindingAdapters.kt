package com.myrgb.ledcontroller.feature.rgbshow

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.myrgb.ledcontroller.R

@BindingAdapter("start_stop_icon")
fun startStopIcon(imageView: ImageView, started: Boolean) {
    if (started)
        imageView.setImageResource(R.drawable.ic_stop)
    else
        imageView.setImageResource(R.drawable.ic_start_rgb_show)
}
