package com.example.ledcontroller.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.ledcontroller.R
import com.example.ledcontroller.adapter.AlarmAdapter
import com.example.ledcontroller.databinding.FragmentAlarmListBinding
import com.example.ledcontroller.databinding.FragmentControllerBinding
import com.example.ledcontroller.viewmodel.AlarmClockViewModel

class AlarmClockListFragment : Fragment() {

    private val viewModel: AlarmClockViewModel by viewModels()
    private lateinit var binding: FragmentAlarmListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewAlarms.adapter = AlarmAdapter()
        binding.recyclerViewAlarms.setHasFixedSize(true)
    }
}