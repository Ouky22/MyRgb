package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmAddEditBinding
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.domain.Weekday
import kotlinx.coroutines.launch
import javax.inject.Inject

class RgbAlarmAddEditFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RgbAlarmAddEditViewModel> { viewModelFactory }

    private lateinit var binding: FragmentRgbAlarmAddEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_rgb_alarm_add_edit, container, false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireContext().applicationContext as App).appComponent.rgbAlarmAddEditComponent()
            .create().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationArgs: RgbAlarmAddEditFragmentArgs by navArgs()
        if (navigationArgs.alarmTime > -1)
            viewModel.setRgbAlarmForEditing(navigationArgs.alarmTime)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rgbAlarmToAddOrEdit.collect {
                    binding.rgbAlarm = it
                    updateCheckedStateOfDayButtons(it)
                }
            }
        }

        setClickListenerOfDayButtons()
    }

    private fun updateCheckedStateOfDayButtons(rgbAlarm: RgbAlarm) {
        binding.btnMonday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.MONDAY)
        binding.btnTuesday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.TUESDAY)
        binding.btnWednesday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.WEDNESDAY)
        binding.btnThursday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.THURSDAY)
        binding.btnFriday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.FRIDAY)
        binding.btnSaturday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.SATURDAY)
        binding.btnSunday.isChecked = rgbAlarm.isRepetitiveOn(Weekday.SUNDAY)
    }

    private fun setClickListenerOfDayButtons() {
        binding.btnMonday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.MONDAY)
        }
        binding.btnTuesday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.TUESDAY)
        }
        binding.btnWednesday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.WEDNESDAY)
        }
        binding.btnThursday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.THURSDAY)
        }
        binding.btnFriday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.FRIDAY)
        }
        binding.btnSaturday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.SATURDAY)
        }
        binding.btnSunday.setOnClickListener {
            viewModel.toggleRepetitiveStatusForWeekday(Weekday.SUNDAY)
        }
    }
}