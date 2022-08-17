package com.example.ledcontroller.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ledcontroller.R
import com.example.ledcontroller.adapter.AlarmAdapter
import com.example.ledcontroller.model.RgbAlarm

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

@BindingAdapter("alarm_list_data")
fun alarmListData(recyclerView: RecyclerView, rgbAlarms: List<RgbAlarm>?) {
    (recyclerView.adapter as AlarmAdapter).submitList(rgbAlarms)
}