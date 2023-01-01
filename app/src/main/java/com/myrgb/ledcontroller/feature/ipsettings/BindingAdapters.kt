package com.myrgb.ledcontroller.feature.ipsettings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.domain.RgbAlarm
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.ceil

@BindingAdapter("ip_address_list_data")
fun ipAddressData(recyclerView: RecyclerView, ipAddressNamePairs: List<IpAddressNamePair>?) {
    (recyclerView.adapter as IpAddressListAdapter).submitList(ipAddressNamePairs)
}

@BindingAdapter("trigger_time_of")
fun triggerTimeOf(textView: MaterialTextView, nextAlarm: RgbAlarm?) {
    if (nextAlarm == null) {
        textView.text = textView.context.getString(R.string.no_alarms_active)
        return
    }

    val nextTriggerDateTimeSeconds = nextAlarm.nextTriggerDateTime.toEpochSecond(ZoneOffset.UTC)
    val currentDateTimeSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    val diffInMinutes = ceil((nextTriggerDateTimeSeconds - currentDateTimeSeconds).toDouble() / 60)

    val hour = (diffInMinutes / 60).toInt()
    val minute = (diffInMinutes % 60).toInt()
    val hourString =
        textView.resources.getQuantityString(R.plurals.trigger_time_hour, hour, hour)
    val minuteString =
        textView.resources.getQuantityString(R.plurals.trigger_time_minute, minute, minute)

    textView.text =
        if (hour == 0)
            textView.context.getString(R.string.next_alarm_in_only_minute_or_hour, minuteString)
        else if (minute == 0)
            textView.context.getString(R.string.next_alarm_in_only_minute_or_hour, hourString)
        else
            textView.context.getString(R.string.next_alarm_in_full, hourString, minuteString)
}