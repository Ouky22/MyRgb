package com.example.ledcontroller.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ledcontroller.R
import com.example.ledcontroller.databinding.FragmentRgbAlarmAddEditBinding
import com.example.ledcontroller.model.RgbAlarm

class RgbAlarmAddEditFragment : Fragment() {

    private val navigationArgs: RgbAlarmAddEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentRgbAlarmAddEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rgb_alarm_add_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.alarmId

        // TODO get alarm from shared vieModel with given id
        binding.rgbAlarm = RgbAlarm(0, 260, false)
    }
}