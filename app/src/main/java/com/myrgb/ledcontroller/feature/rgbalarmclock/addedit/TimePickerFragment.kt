package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class TimePickerFragment(
    private val onTimeSet: (timeMinutes: Int) -> Unit
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentTime = LocalTime.now()
        val currentHour = currentTime.hour
        val currentMinute = currentTime.minute

        return TimePickerDialog(
            requireContext(),
            this,
            currentHour,
            currentMinute,
            DateFormat.is24HourFormat(requireContext())
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        onTimeSet(hourOfDay * 60 + minute)
    }
}