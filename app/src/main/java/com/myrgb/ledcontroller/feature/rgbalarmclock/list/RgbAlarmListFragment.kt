package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmListBinding
import com.myrgb.ledcontroller.di.RgbAlarmContainer
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmViewModel

class RgbAlarmListFragment : Fragment() {

    private lateinit var viewModel: RgbAlarmViewModel

    private lateinit var binding: FragmentRgbAlarmListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_alarm_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (requireActivity().application as App).appContainer
        if (appContainer.rgbAlarmContainer == null)
            appContainer.rgbAlarmContainer = RgbAlarmContainer(appContainer.defaultRgbAlarmRepository)
        appContainer.rgbAlarmContainer?.let {
            val vm: RgbAlarmViewModel by viewModels {
                it.rgbAlarmViewModelFactory
            }
            viewModel = vm
            binding.viewModel = viewModel
        }

        binding.recyclerViewAlarms.adapter = RgbAlarmListAdapter { rgbAlarm ->
            val action = RgbAlarmListFragmentDirections.actionAlarmListToAlarmAddEdit(rgbAlarm.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewAlarms.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity().application as App).appContainer.rgbAlarmContainer = null
    }
}