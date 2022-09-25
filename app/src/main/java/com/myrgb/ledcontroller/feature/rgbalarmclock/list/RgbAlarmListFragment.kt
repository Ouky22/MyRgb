package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmListBinding
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmViewModel
import javax.inject.Inject

class RgbAlarmListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<RgbAlarmViewModel> { viewModelFactory }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireContext().applicationContext as App).appComponent.rgbAlarmComponent().create()
            .inject(this)
    }
}