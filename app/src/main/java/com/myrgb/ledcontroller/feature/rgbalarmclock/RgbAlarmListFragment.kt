package com.myrgb.ledcontroller.feature.rgbalarmclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmListBinding

class RgbAlarmListFragment : Fragment() {

    private val viewModel: RgbAlarmViewModel by lazy {
        val activity =
            requireNotNull(this.activity) { "ViewModel available after onActivityCreated()" }
        ViewModelProvider(
            this, RgbAlarmViewModel.Factory(activity.application)
        )[RgbAlarmViewModel::class.java]
    }

    private lateinit var binding: FragmentRgbAlarmListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_alarm_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewAlarms.adapter = RgbAlarmListAdapter { rgbAlarm ->
            val action = RgbAlarmListFragmentDirections.actionAlarmListToAlarmAddEdit(rgbAlarm.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewAlarms.setHasFixedSize(true)
    }
}