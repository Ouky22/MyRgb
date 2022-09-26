package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.databinding.FragmentRgbAlarmAddEditBinding
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet

class RgbAlarmAddEditFragment : Fragment() {

    private val navigationArgs: RgbAlarmAddEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentRgbAlarmAddEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rgb_alarm_add_edit,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO get alarm from shared vieModel with given id
        binding.rgbAlarm = RgbAlarm(260, false, RgbTriplet(255, 200, 0))
    }
}