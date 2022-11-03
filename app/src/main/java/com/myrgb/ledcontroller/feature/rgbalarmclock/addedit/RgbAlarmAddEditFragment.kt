package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmAddEditBinding
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.extensions.collectLatestLifecycleFlow
import com.myrgb.ledcontroller.extensions.coordinateIsInsideView
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerFragmentDirections
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
        if (navigationArgs.alarmTime > -1) {
            viewModel.setRgbAlarmForEditing(navigationArgs.alarmTime)
            setupMenu()
        }

        collectLatestLifecycleFlow(viewModel.rgbAlarmToAddOrEdit) {
            binding.rgbAlarm = it
            updateCheckedStateOfDayButtons(it)
        }
        collectLatestLifecycleFlow(viewModel.dataSaved) { dataSaved ->
            if (dataSaved) findNavController().popBackStack()
        }

        setClickListenerOfDayButtons()
        binding.btnSave.setOnClickListener { viewModel.saveRgbAlarm() }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
        binding.tvTriggerTime.setOnClickListener {
            TimePickerFragment { timeMinutes ->
                viewModel.setTime(timeMinutes)
            }.show(parentFragmentManager, "alarmTimePicker")
        }

        binding.root.setOnTouchListener { v, e ->
            v.performClick()

            if (e.action != MotionEvent.ACTION_DOWN && e.action != MotionEvent.ACTION_MOVE)
                return@setOnTouchListener true

            val touchPositionX = e.x
            val touchPositionY = e.y
            if (binding.ivRgbCircle.coordinateIsInsideView(touchPositionX, touchPositionY))
                viewModel.setAlarmColorOnRgbCircleTouch(
                    touchPositionX.toInt(),
                    touchPositionY.toInt()
                )

            return@setOnTouchListener true
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            viewModel.rgbCircleCenterX = binding.ivRgbCircle.left + (binding.ivRgbCircle.width / 2)
            viewModel.rgbCircleCenterY = binding.ivRgbCircle.top + (binding.ivRgbCircle.height / 2)
        }
    }

    private fun setupMenu() {
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_rgb_alarm_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId != R.id.action_delete_rgb_alarm)
                    return false

                viewModel.deleteAlarm()
                findNavController().navigate(
                    RgbAlarmAddEditFragmentDirections.actionAlarmAddEditToAlarmList()
                )
                return true
            }
        }, viewLifecycleOwner)
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