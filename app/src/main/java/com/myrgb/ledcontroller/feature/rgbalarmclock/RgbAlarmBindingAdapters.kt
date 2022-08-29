package com.myrgb.ledcontroller.feature.rgbalarmclock

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListAdapter


@BindingAdapter("alarm_list_data")
fun alarmListData(recyclerView: RecyclerView, rgbAlarms: List<RgbAlarm>?) {
    (recyclerView.adapter as RgbAlarmListAdapter).submitList(rgbAlarms)
}